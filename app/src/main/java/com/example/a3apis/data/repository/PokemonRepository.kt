package com.example.a3apis.data.repository

import com.example.a3apis.data.mapper.UnifiedMapper
import com.example.a3apis.data.model.UnifiedCard
import com.example.a3apis.data.remote.api.ApiClient
import com.example.a3apis.data.remote.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PokemonRepository(
    private val apiService: ApiService = ApiClient.apiService
) {

    suspend fun get3Pokemon(): Result<List<UnifiedCard>> = withContext(Dispatchers.IO) {
        try {
            val randomIds = (1..3).map { Random.nextInt(1, 250).toString() }
            val deferredList = randomIds.map { id ->
                async {
                    val response = apiService.getPokemon(id)
                    if (response.isSuccessful && response.body() != null) {
                        UnifiedMapper.mapPokemonToUnified(response.body()!!)
                    } else null
                }
            }
            val results = deferredList.awaitAll().filterNotNull()
            if (results.isNotEmpty()) Result.success(results)
            else Result.failure(Exception("Error al obtener Pokémon"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPokemonByIdOrName(idOrName: String): Result<UnifiedCard> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPokemon(idOrName)
            if (response.isSuccessful && response.body() != null) {
                Result.success(UnifiedMapper.mapPokemonToUnified(response.body()!!))
            } else {
                Result.failure(Exception("No se encontró el Pokémon $idOrName"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
