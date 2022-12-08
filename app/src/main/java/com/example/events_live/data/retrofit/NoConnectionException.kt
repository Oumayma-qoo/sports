package com.example.events_live.data.retrofit

import android.content.Context

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NoConnectionException(private val context: Context): IOException() {
    override val message: String
        get() =context.getString(1)
}

class NoInternetException(private val context: Context) : IOException() {
    override val message: String
        get() = context.getString(1)
}

class RequestInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .build()
        return chain.proceed(newRequest)
    }
}