package com.felix.resp

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.resp
 * @ClassName: NeteaseImpl
 * @Author: 80341341
 * @CreateDate: 2021/8/3 20:42
 * @Description: NeteaseImpl 类作用描述
 */
class NeteaseImpl : IRes {
    override suspend fun searchMp3(name: String, page: Int): List<Mp3Bean> {
        return ApiNeteaseProxy.searchMp3(query = name, page = page + 1)
            ?.takeIf { it.code == 200 }?.data?.map { it.toMp3Bean() } ?: emptyList()
    }
}