package com.felix.resp

import com.felix.utils.gson.fromJson
import com.felix.utils.gson.toJson
import com.felix.utils.utils.encrypt.md5

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.resp
 * @ClassName: DbRes
 * @Author: 80341341
 * @CreateDate: 2021/8/4 19:15
 * @Description: DbRes 类作用描述
 */
class DbCacheRes : ICacheableRes {
    private val CACHE_TIME = 24 * 60 * 60_000
    override suspend fun searchMp3(name: String, page: Int, type: String): List<Mp3Bean> {
        return genKey(name, page, type)?.let {
            RespDatabase.instance.mp3CacheDao().query(it)
        }?.let {
            it.data.fromJson<List<Mp3Bean>>()
        } ?: emptyList()
    }

    override suspend fun cache(name: String, page: Int, type: String, data: List<Mp3Bean>) {
        data.takeIf {
            it.isNotEmpty()
        }?.let {
            it.toJson()
        }?.let { data ->
            genKey(name, page, type)?.let {
                Mp3Cache(
                    id = it,
                    data = data,
                    expireTime = System.currentTimeMillis() + CACHE_TIME
                )
            }
        }?.let {
            RespDatabase.instance.mp3CacheDao().insert(it)
        }
    }

    private fun genKey(name: String, page: Int, type: String): String? {
        return (name + page + type).md5()
    }
}