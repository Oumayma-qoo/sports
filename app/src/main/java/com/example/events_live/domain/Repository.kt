package com.example.events_live.domain

import com.example.events_live.common.utils.DataState
import com.example.events_live.data.endpoints.Api
import com.example.events_live.domain.models.BaseClassIndexNew
import com.example.events_live.domain.models.event.EventBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api

) : ApiRepo {

    override suspend fun getHomeMatches(
        locale: String,
        pageNumber: String
    ): Flow<DataState<BaseClassIndexNew>> = flow {
        emit(DataState.Success(api.getHomeMatchesData(locale, pageNumber)))

    }

    override suspend fun getEvents(): Flow<DataState<EventBase>> = flow {
        emit(DataState.Success(api.getEventsData()))
    }


}
