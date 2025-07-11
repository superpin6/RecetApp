package com.universitatcarlemany.recetapp.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.universitatcarlemany.recetapp.adapter.RecetaAdapter
import com.universitatcarlemany.recetapp.databinding.ActivityFavoritesBinding
import com.universitatcarlemany.recetapp.model.database.AppDatabase
import com.universitatcarlemany.recetapp.model.entity.FavoriteRecipeEntity
import com.universitatcarlemany.recetapp.model.entity.Receta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: RecetaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Room with fallback (safe for dev)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "recetapp-db"
        ).fallbackToDestructiveMigration().build()
        val favoritesDao = db.favoriteRecipeDao()

        lifecycleScope.launch {
            val favoritosEntities = withContext(Dispatchers.IO) {
                favoritesDao.getAll()
            }
            val favoritos = favoritosEntities.map { it.toReceta() }

            adapter = RecetaAdapter(
                favoritos,
                isFavorite = { true }, // All displayed here are favorite
                onToggleFavorite = { receta, position ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            favoritesDao.delete(receta.toFavoriteEntity())
                        }
                        // Reload favorites after removal.
                        val nuevosFavoritos = withContext(Dispatchers.IO) {
                            favoritesDao.getAll().map { it.toReceta() }
                        }
                        adapter.updateData(nuevosFavoritos)
                        // Optionally, you could notify only this item (if deleted, just reload all)
                    }
                },
                onRecetaClick = { receta ->
                    // Open detail screen for selected recipe
                    val intent = Intent(this@FavoritesActivity, RecetaDetalleActivity::class.java)
                    intent.putExtra("receta_id", receta.id)
                    startActivity(intent)
                }
            )

            binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(this@FavoritesActivity)
            binding.recyclerViewFavoritos.adapter = adapter
        }
    }

    // Conversion function: transforms FavoriteRecipeEntity into a Receta
    private fun FavoriteRecipeEntity.toReceta() = Receta(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        categoria = this.categoria,
        foto = this.foto,
        ingredientes = this.ingredientes,
        preparacion = this.preparacion
    )

    // Extension: Receta -> FavoriteRecipeEntity.
    private fun Receta.toFavoriteEntity() = FavoriteRecipeEntity(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        categoria = this.categoria,
        foto = this.foto,
        ingredientes = this.ingredientes,
        preparacion = this.preparacion
    )
}
