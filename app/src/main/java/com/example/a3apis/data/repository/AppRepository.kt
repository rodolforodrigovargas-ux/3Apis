package com.example.a3apis.data.repository

import com.example.a3apis.data.model.UnifiedCard

/**
 * Contenedor simple de los 3 Repositorios
 */
class AppRepository(
    val pokemonRepository: PokemonRepository = PokemonRepository(),
    val tarotRepository: TarotRepository = TarotRepository(),
    val adviceRepository: AdviceRepository = AdviceRepository()
) {

    suspend fun getAll3OfEach(): Result<List<UnifiedCard>> {
        val pokemonResult = pokemonRepository.get3Pokemon().getOrDefault(emptyList())
        val tarotResult = tarotRepository.get3TarotCards().getOrDefault(emptyList())
        val adviceResult = adviceRepository.get3Advices().getOrDefault(emptyList())

        val combined = pokemonResult + tarotResult + adviceResult
        return if (combined.isNotEmpty()) Result.success(combined)
        else Result.failure(Exception("Error al cargar datos"))
    }
}
