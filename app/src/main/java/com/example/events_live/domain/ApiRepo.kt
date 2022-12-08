package com.example.events_live.domain

import com.example.events_live.common.utils.DataState
import com.example.events_live.domain.models.BaseClassIndexNew
import com.example.events_live.domain.models.event.EventBase

import kotlinx.coroutines.flow.Flow

interface ApiRepo {

    suspend fun getHomeMatches(locale : String, pageNumber : String) : Flow<DataState<BaseClassIndexNew>>

    suspend fun getEvents() : Flow<DataState<EventBase>>
}