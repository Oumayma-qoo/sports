package com.example.events_live.presentation.home

import com.example.events_live.domain.models.BaseClassIndexNew
import com.example.events_live.domain.models.event.EventBase


sealed class HomeScreenState{


    object Init : HomeScreenState()
    data class IsLoading(val isLoading: Boolean) : HomeScreenState()
    data class NoInternetException(val message:String): HomeScreenState()
    data class GeneralException(val message:String): HomeScreenState()
    data class StatusFailed(val message: String) : HomeScreenState()
    data class Response(val videos: BaseClassIndexNew) : HomeScreenState()

}
