package com.example.a3apis.data.repository

import com.example.a3apis.data.mapper.UnifiedMapper
import com.example.a3apis.data.model.UnifiedCard
import com.example.a3apis.data.remote.api.ApiClient
import com.example.a3apis.data.remote.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TarotRepository(
    private val apiService: ApiService = ApiClient.apiService
) {

    suspend fun get3TarotCards(): Result<List<UnifiedCard>> = getRandomTarotCards(3)

    suspend fun getRandomTarotCards(count: Int): Result<List<UnifiedCard>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getRandomTarotCards(count = count)
            if (response.isSuccessful && response.body() != null) {
                val cards = response.body()!!.cards.take(count).map { cardDto ->
                    UnifiedMapper.mapTarotToUnified(cardDto)
                }
                Result.success(cards)
            } else {
                Result.failure(Exception("Error al obtener cartas de Tarot"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
