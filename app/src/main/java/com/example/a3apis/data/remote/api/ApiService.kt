package com.example.a3apis.data.remote.api

import com.example.a3apis.data.remote.model.AdviceResponseDto
import com.example.a3apis.data.remote.model.PokemonDto
import com.example.a3apis.data.remote.model.TarotResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // 1. PokéAPI Endpoint
    @GET("https://pokeapi.co/api/v2/pokemon/{idOrName}")
    suspend fun getPokemon(
        @Path("idOrName") idOrName: String
    ): Response<PokemonDto>

    // 2. Tarot API Endpoint
    @GET("https://tarotapi.dev/api/v1/cards/random")
    suspend fun getRandomTarotCards(
        @Query("n") count: Int = 3
    ): Response<TarotResponseDto>

    // 3. Advice Slip API Endpoint
    @GET("https://api.adviceslip.com/advice")
    suspend fun getRandomAdvice(): Response<AdviceResponseDto>
}
