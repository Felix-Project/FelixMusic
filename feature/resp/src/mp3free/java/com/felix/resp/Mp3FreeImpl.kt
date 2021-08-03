package com.felix.resp

class Mp3FreeImpl : IRes {
    override suspend fun searchMp3(name: String, page: Int): List<Mp3Bean> {
        return ApiServiceProxy.searchMp3(" $name", " $page")?.response?.map {
            it.toMp3Bean()
        } ?: emptyList()
    }
}