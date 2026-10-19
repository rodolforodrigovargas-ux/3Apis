package com.example.a3apis.data.remote.model

import com.google.gson.annotations.SerializedName

data class TarotResponseDto(
    @SerializedName("nhits") val nhits: Int? = null,
    @SerializedName("cards") val cards: List<TarotCardDto>
)

data class TarotCardDto(
    @SerializedName("name_short") val nameShort: String,
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: String,
    @SerializedName("value_int") val valueInt: Int,
    @SerializedName("type") val type: String,
    @SerializedName("meaning_up") val meaningUp: String,
    @SerializedName("meaning_rev") val meaningRev: String,
    @SerializedName("desc") val desc: String
)
