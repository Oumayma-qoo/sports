package com.example.events_live.common.utils

sealed class DataState<out T> {
    data class Success<out T>(val value: T) : DataState<T>()
    data class GenericError(val code: Int? = null, val error: Error? = null) :
        DataState<Nothing>()
}