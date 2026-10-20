package com.example.a3apis.data.repository

import android.content.Context
import com.example.a3apis.data.local.AppDatabase
import com.example.a3apis.data.model.UnifiedCard

/**
 * Contenedor de Repositorios con soporte para Favoritos en Room
 */
class AppRepository(
    private val context: Context? = null
) {

    private val db: AppDatabase? by lazy {
        context?.applicationContext?.let { AppDatabase.getDatabase(it) }
    }

    val pokemonRepository: PokemonRepository by lazy { PokemonRepository() }
    val tarotRepository: TarotRepository by lazy { TarotRepository() }
    val adviceRepository: AdviceRepository by lazy { AdviceRepository() }

    val favoritosRepository: FavoritosRepository? by lazy {
        db?.let { FavoritosRepository(it.favoritoDao()) }
    }

    suspend fun getAll3OfEach(): Result<List<UnifiedCard>> {
        val pokemonResult = pokemonRepository.get3Pokemon().getOrDefault(emptyList())
        val tarotResult = tarotRepository.get3TarotCards().getOrDefault(emptyList())
        val adviceResult = adviceRepository.get3Advices().getOrDefault(emptyList())

        val combined = pokemonResult + tarotResult + adviceResult
        return if (combined.isNotEmpty()) Result.success(combined)
        else Result.failure(Exception("Error al cargar datos de las APIs"))
    }
}
