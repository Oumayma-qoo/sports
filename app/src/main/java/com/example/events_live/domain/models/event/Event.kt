package com.example.events_live.domain.models.event

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("event")
    var event: List<EventX> = listOf(),
    @SerializedName("matchId")
    var matchId: Int = 0
)