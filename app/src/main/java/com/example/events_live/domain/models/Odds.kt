package com.example.events_live.domain.models

import com.google.gson.annotations.SerializedName


data class Odds(
    @SerializedName("europeOdds")
    var europeOdds: List<Any>? = listOf(),
    @SerializedName("handicap")
    var handicap: List<Any>? = listOf(),
    @SerializedName("handicapHalf")
    var handicapHalf: List<Any>? = listOf(),
    @SerializedName("overUnder")
    var overUnder: List<Any>? = listOf(),
    @SerializedName("overUnderHalf")
    var overUnderHalf: List<Any>? = listOf()
)
