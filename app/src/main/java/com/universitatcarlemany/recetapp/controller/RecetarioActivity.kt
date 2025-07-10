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

        // Set up custom Toolbar and hide default title
        setSupportActionBar(binding.toolbarRecetario)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle back arrow click
        binding.toolbarRecetario.setNavigationOnClickListener {
            finish()
        }

        // Register launcher for notification permissions (Android 13+)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.entries.all { it.value }) {
                Toast.makeText(this, "Notification permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notification permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

        // Ask for notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(arrayOf(permission))
            }
        }

        // Create notification channel (if needed)
        NotificationHelper.createFavoritosChannel(this)

        // Initialize database and DAO for favorites
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "recetapp-db"
        ).fallbackToDestructiveMigration().build()
        val favoritesDao = db.favoriteRecipeDao()

        // Load all recipes from JSON (assets)
        recetasOriginal = cargarRecetasDesdeAssets().toMutableList()

        // Set up the adapter with favorite functionality and click listeners
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
                    // Show notification if added to favorites
                    if (wasAdded) {
                        NotificationHelper.showFavoriteAdded(
                            context = this@RecetarioActivity,
                            recetaNombre = receta.nombre
                        )
                    }
                    adapter.notifyItemChanged(position)
                }
            },
            onRecetaClick = { receta ->
                // Open detail activity
                val intent = Intent(this, RecetaDetalleActivity::class.java)
                intent.putExtra("receta_id", receta.id)
                startActivity(intent)
            }
        )

        // Set up RecyclerView
        binding.recyclerViewRecetas.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRecetas.adapter = adapter

        // Filter recipes by category using Spinner
        val categorias = resources.getStringArray(R.array.categorias_array)
        binding.spinnerCategorias.setSelection(0)
        binding.spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val selectedCategory = categorias[pos]
                val filteredList = if (selectedCategory == "Todas") {
                    recetasOriginal
                } else {
                    recetasOriginal.filter { it.categoria.equals(selectedCategory, ignoreCase = true) }
                }
                adapter.updateData(filteredList)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // Enable navigation back with ActionBar's arrow
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // Extension function: Convert Receta to FavoriteRecipeEntity for Room
    private fun Receta.toEntity() = FavoriteRecipeEntity(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        categoria = this.categoria,
        foto = this.foto,
        ingredientes = this.ingredientes,
        preparacion = this.preparacion
    )

    // Load recipes from assets/recetas.json
    private fun cargarRecetasDesdeAssets(): List<Receta> {
        return try {
            val json = assets.open("recetas.json")
                .bufferedReader()
                .use { it.readText() }
            val listType = object : TypeToken<List<Receta>>() {}.type
            Gson().fromJson(json, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
