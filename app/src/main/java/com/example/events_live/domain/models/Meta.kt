package com.example.events_live.domain.models

import com.google.gson.annotations.SerializedName

data class Meta (
    @SerializedName("currentPage")
    var currentPage: Int = 0,
    @SerializedName("lastPage")
    var lastPage: Int = 0,
    @SerializedName("perPage")
    var perPage: Int = 0,
    @SerializedName("total")
    var total: Int = 0
        )