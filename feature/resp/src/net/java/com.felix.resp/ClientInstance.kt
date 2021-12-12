package com.felix.resp

import com.felix.utils.AppProxy
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.resp
 * @ClassName: ClientInstance
 * @Author: 80341341
 * @CreateDate: 2021/8/4 18:29
 * @Description: ClientInstance 类作用描述
 */

private val logger =
    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

private class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control", "public, max-age=${24 * 60 * 60}") //缓存一天
            .build()
        return chain.proceed(newRequest)
    }
}

internal val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(CacheInterceptor())
    .addInterceptor(logger)
    .cache(Cache(AppProxy.externalCacheDir!!, 20L * 1024 * 1024))
    .build()