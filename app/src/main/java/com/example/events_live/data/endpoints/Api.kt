package com.example.events_live.data.endpoints

import com.example.events_live.domain.models.BaseClassIndexNew
import com.example.events_live.domain.models.event.EventBase
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/api/zqbf-list-page/{locale}/{page}")
    suspend fun getHomeMatchesData(@Path("locale") locale: String, @Path("page") pageNumber: String) : BaseClassIndexNew

    @GET("/api/zqbf-list-event/")
    suspend fun getEventsData(): EventBase

}