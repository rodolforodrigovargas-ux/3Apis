package com.example.a3apis.data.repository

import com.example.a3apis.data.mapper.UnifiedMapper
import com.example.a3apis.data.model.UnifiedCard
import com.example.a3apis.data.remote.api.ApiClient
import com.example.a3apis.data.remote.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class AdviceRepository(
    private val apiService: ApiService = ApiClient.apiService
) {

    suspend fun get3Advices(): Result<List<UnifiedCard>> = withContext(Dispatchers.IO) {
        try {
            val deferredList = (1..3).map {
                async {
                    val response = apiService.getRandomAdvice()
                    if (response.isSuccessful && response.body() != null) {
                        UnifiedMapper.mapAdviceToUnified(response.body()!!.slip)
                    } else null
                }
            }
            val results = deferredList.awaitAll().filterNotNull()
            if (results.isNotEmpty()) Result.success(results)
            else Result.failure(Exception("Error al obtener consejos"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomAdvice(): Result<UnifiedCard> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getRandomAdvice()
            if (response.isSuccessful && response.body() != null) {
                Result.success(UnifiedMapper.mapAdviceToUnified(response.body()!!.slip))
            } else {
                Result.failure(Exception("Error al obtener un consejo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
