package com.universitatcarlemany.recetapp.controller

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecetarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Modern permission launcher for notifications
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.entries.all { it.value }) {
                Toast.makeText(this, "Notification permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

        // Request notification permission only on Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permiso = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permiso) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(arrayOf(permiso))
            }
        }

        // Create notification channel (only needed once)
        NotificationHelper.createFavoritosChannel(this)

        // Initialize Room database with fallback (destructive migration for dev only)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "recetapp-db"
        ).fallbackToDestructiveMigration().build()
        val favoritesDao = db.favoriteRecipeDao()

        // Load recipes from assets as a mutable list
        recetasOriginal = cargarRecetasDesdeAssets().toMutableList()

        // Set up the adapter with favorite logic and navigation
        adapter = RecetaAdapter(
            recetasOriginal,
            isFavorite = { id ->
                withContext(Dispatchers.IO) {
                    favoritesDao.getById(id) != null
                }
            },
            onToggleFavorite = { receta, position ->
                lifecycleScope.launch {
                    val wasAdded = withContext(Dispatchers.IO) {
                        val entity = receta.toEntity()
                        val exists = favoritesDao.getById(receta.id) != null
                        if (exists) {
                            favoritesDao.delete(entity)
                            false
                        } else {
                            favoritesDao.insert(entity)
                            true
                        }
                    }
                    if (wasAdded) {
                        NotificationHelper.showFavoriteAdded(
                            context = this@RecetarioActivity,
                            recetaNombre = receta.nombre
                        )
                    }
                    // Update only this item in the adapter for better performance
                    adapter.notifyItemChanged(position)
                }
            },
            onRecetaClick = { receta ->
                // Navigate to detail screen with the selected recipe's ID
                val intent = Intent(this, RecetaDetalleActivity::class.java)
                intent.putExtra("receta_id", receta.id)
                startActivity(intent)
            }
        )

        // Attach the adapter and layout manager to RecyclerView
        binding.recyclerViewRecetas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecetas.adapter = adapter

        // --- Filter recipes by category using Spinner ---
        val categorias = resources.getStringArray(R.array.categorias_array)
        binding.spinnerCategorias.setSelection(0)
        binding.spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val categoriaElegida = categorias[pos]
                val filtradas = if (categoriaElegida == "Todas") {
                    recetasOriginal
                } else {
                    recetasOriginal.filter { it.categoria.equals(categoriaElegida, ignoreCase = true) }
                }
                adapter.updateData(filtradas)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Converts Receta (model) to FavoriteRecipeEntity for Room
    private fun Receta.toEntity() = FavoriteRecipeEntity(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        categoria = this.categoria,
        foto = this.foto,
        ingredientes = this.ingredientes,
        preparacion = this.preparacion
    )

    // Load recipes from a JSON file in assets
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
