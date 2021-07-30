package com.felix.resp

import com.felix.resp.converter.Mp3ConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("/api/search.php?callback=")
    suspend fun searchMp3(
        @Field("q") query: String,
        @Field("page") page: String = "0"
    ): Mp3FreeResp

    companion object {
        private const val BASE_URL = "https://myfreemp3juices.cc"

        fun create(): ApiService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(Mp3ConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}

val ApiServiceProxy: ApiService = ApiService.create()