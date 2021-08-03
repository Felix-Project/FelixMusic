package com.felix.resp

interface IRes {
    suspend fun searchMp3(name: String, page: Int = 0): List<Mp3Bean>
}

object ResProxy : IRes {
    var iRes: IRes? = null
    override suspend fun searchMp3(name: String, page: Int): List<Mp3Bean> {
        return iRes?.searchMp3(name, page) ?: emptyList()
    }
}