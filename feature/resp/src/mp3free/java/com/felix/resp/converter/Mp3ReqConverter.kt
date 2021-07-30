package com.felix.resp.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.Charset

class Mp3ReqConverter<T>(var gson: Gson, var adapter: TypeAdapter<T>) :
    Converter<T, RequestBody> {
    private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
    private val UTF_8 = Charset.forName("UTF-8")

    override fun convert(value: T): RequestBody? {
        val buffer = Buffer()
        val writer: Writer =
            OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}