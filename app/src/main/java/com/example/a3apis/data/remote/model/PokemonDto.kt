package com.example.a3apis.data.remote.model

import com.google.gson.annotations.SerializedName

data class PokemonDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sprites") val sprites: PokemonSpritesDto,
    @SerializedName("types") val types: List<PokemonTypeSlotDto>
)

data class PokemonSpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("other") val other: PokemonOtherSpritesDto?
)

data class PokemonOtherSpritesDto(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtworkDto?
)

data class OfficialArtworkDto(
    @SerializedName("front_default") val frontDefault: String?
)

data class PokemonTypeSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: PokemonTypeDto
)

data class PokemonTypeDto(
    @SerializedName("name") val name: String
)
