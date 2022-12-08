package com.example.events_live.presentation.event

import com.example.events_live.domain.models.event.EventBase

sealed class EventScreenState {

    object Init : EventScreenState()
    data class IsLoading(val isLoading: Boolean) : EventScreenState()
    data class NoInternetException(val message:String): EventScreenState()
    data class GeneralException(val message:String): EventScreenState()
    data class StatusFailed(val message: String) : EventScreenState()
    data class Response(val events : EventBase) : EventScreenState()
}