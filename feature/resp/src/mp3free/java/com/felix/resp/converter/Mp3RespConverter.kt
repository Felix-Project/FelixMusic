package com.felix.resp.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.StringReader


class Mp3RespConverter<T>(var gson: Gson, var adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        return value.string()?.let {
            var length = it.length - 1
            var i = 0
            if (it[0] == '(') {
                i++
            }
            if (it[length] == ';') {
                length--
            }
            if (it[length] == ')') {
                length--
            }
            it.substring(i, length + 1)
        }?.let {
            it.replaceFirst("\"apple\",", "")
        }?.let {
            val jsonReader = gson.newJsonReader(StringReader(it))
            return try {
                val result = adapter.read(jsonReader)
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw JsonIOException("JSON document was not fully consumed.")
                }
                result
            } finally {
                value.close()
            }
        }
    }
}