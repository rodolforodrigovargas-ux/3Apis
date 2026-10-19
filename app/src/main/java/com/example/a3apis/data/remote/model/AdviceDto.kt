package com.example.a3apis.data.remote.model

import com.google.gson.annotations.SerializedName

data class AdviceResponseDto(
    @SerializedName("slip") val slip: AdviceSlipDto
)

data class AdviceSlipDto(
    @SerializedName("id") val id: Int,
    @SerializedName("advice") val advice: String
)
