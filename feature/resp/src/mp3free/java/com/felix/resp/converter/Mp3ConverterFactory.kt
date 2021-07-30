package com.felix.resp.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class Mp3ConverterFactory(var gson: Gson) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return Mp3RespConverter(gson, adapter as TypeAdapter<Any>)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return Mp3ReqConverter(gson, adapter as TypeAdapter<Any>)
    }

    companion object {
        fun create(): Mp3ConverterFactory {
            return Mp3ConverterFactory(Gson())
        }
    }
}