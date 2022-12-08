package com.example.events_live.data.retrofit

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.Exception
import java.lang.reflect.Type
import javax.inject.Inject

class NullOnEmptyConverterFactory @Inject constructor() : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate: Converter<ResponseBody, *> =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return (Converter { body: ResponseBody ->
            if (body.contentLength() == 0L) return@Converter null
            try {
                delegate.convert(body)
            } catch (e: Exception) {
                val responseBody = "{\"success\": false, \"message\": \"Conversion Error\"}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
                delegate.convert(responseBody)
            }
        })
    }
}