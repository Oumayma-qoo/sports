package com.example.events_live.domain.models.event

import com.google.gson.annotations.SerializedName


data class EventX(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("isHome")
    var isHome: Boolean = false,
    @SerializedName("kind")
    var kind: Int = 0,
    @SerializedName("nameChs")
    var nameChs: String = "",
    @SerializedName("nameCht")
    var nameCht: String = "",
    @SerializedName("nameCn")
    var nameCn: String = "",
    @SerializedName("nameEn")
    var nameEn: String = "",
    @SerializedName("nameId")
    var nameId: String = "",
    @SerializedName("nameVi")
    var nameVi: String = "",
    @SerializedName("overtime")
    var overtime: String = "",
    @SerializedName("playerId")
    var playerId: String = "",
    @SerializedName("playerId2")
    var playerId2: String = "",
    @SerializedName("time")
    var time: String = ""
)