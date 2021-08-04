package com.felix.resp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

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
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiNetease::class.java)
        }
    }
}

val ApiNeteaseProxy: ApiNetease = ApiNetease.create()