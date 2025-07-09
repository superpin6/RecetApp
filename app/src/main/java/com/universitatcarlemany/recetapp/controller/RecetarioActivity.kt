package com.universitatcarlemany.recetapp.controller

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.universitatcarlemany.recetapp.R
import com.universitatcarlemany.recetapp.adapter.RecetaAdapter
import com.universitatcarlemany.recetapp.databinding.ActivityRecetarioBinding
import com.universitatcarlemany.recetapp.model.database.AppDatabase
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity
import com.universitatcarlemany.recetapp.model.entity.Receta
import com.universitatcarlemany.recetapp.util.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecetarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecetarioBinding
    private lateinit var adapter: RecetaAdapter
    private lateinit var recetasOriginal: MutableList<Receta>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecetarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pedir permiso de notificaciones SOLO en Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permiso = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permiso), 1001)
            }
        }

        // Inicializa la base de datos Room (ahora con fallbackToDestructiveMigration)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "recetapp-db"
        ).fallbackToDestructiveMigration()
            .build()
        val favoritesDao = db.favoriteRecipeDao()

        // Carga las recetas del JSON como lista mutable
        recetasOriginal = cargarRecetasDesdeAssets().toMutableList()

        adapter = RecetaAdapter(
            recetasOriginal,
            isFavorite = { id ->
                withContext(Dispatchers.IO) {
                    favoritesDao.getById(id) != null
                }
            },
            onToggleFavorite = { receta ->
                lifecycleScope.launch {
                    val wasAdded = withContext(Dispatchers.IO) {
                        val entity = receta.toEntity()
                        val exists = favoritesDao.getById(receta.id) != null
                        if (exists) {
                            favoritesDao.delete(entity)
                            false
                        } else {
                            favoritesDao.insert(entity)
                            true // Solo aquí devolvemos true (cuando se añade)
                        }
                    }
                    if (wasAdded) {
                        NotificationHelper.showFavoriteAdded(
                            context = this@RecetarioActivity,
                            recetaNombre = receta.nombre
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
            },
            onRecetaClick = { receta ->
                // Llama al detalle, pasando el ID de la receta seleccionada
                val intent = Intent(this, RecetaDetalleActivity::class.java)
                intent.putExtra("receta_id", receta.id)
                startActivity(intent)
            }
        )

        binding.recyclerViewRecetas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecetas.adapter = adapter

        // --- Filtrado por categoría con Spinner ---
        val categorias = resources.getStringArray(R.array.categorias_array)
        binding.spinnerCategorias.setSelection(0)
        binding.spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val categoriaElegida = categorias[pos]
                val filtradas = if (categoriaElegida == "Todas") {
                    recetasOriginal // muestra todas
                } else {
                    recetasOriginal.filter { it.categoria.equals(categoriaElegida, ignoreCase = true) }
                }
                adapter.updateData(filtradas)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Conversión de modelo de JSON a Entity de Room
    private fun Receta.toEntity() = FavoriteRecipeEntity(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        categoria = this.categoria,
        foto = this.foto,
        ingredientes = this.ingredientes,
        preparacion = this.preparacion
    )

    // Cargar recetas desde JSON en assets
    private fun cargarRecetasDesdeAssets(): List<Receta> {
        return try {
            val json = assets.open("recetas.json")
                .bufferedReader()
                .use { it.readText() }
            val tipoLista = object : TypeToken<List<Receta>>() {}.type
            Gson().fromJson(json, tipoLista)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
