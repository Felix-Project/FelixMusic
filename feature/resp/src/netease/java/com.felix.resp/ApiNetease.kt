package com.felix.resp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiNetease {
    @Headers("x-requested-with:XMLHttpRequest")
    @FormUrlEncoded
    @POST("/")
    suspend fun searchMp3(
        @Field("input") query: String,
        @Field("page") page: Int = 1,
        @Field("filter") filter: String = "name",
        @Field("type") type: String = "netease"
    ): NeteaseResp

    companion object {
        private const val BASE_URL = "https://music.liuzhijin.cn/"

        fun create(): ApiNetease {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiNetease::class.java)
        }
    }
}

val ApiNeteaseProxy: ApiNetease = ApiNetease.create()