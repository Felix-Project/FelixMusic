package com.felix.resp

interface IRes {
    suspend fun searchMp3(name: String, page: Int = 0, type: String = ""): List<Mp3Bean>
}

object ResProxy : IRes {
    var iMp3FreeRes: IRes? = null
    var iRes: IRes? = null

    override suspend fun searchMp3(name: String, page: Int, type: String): List<Mp3Bean> {
        if (TypeProxy == Type.mp3free) {
            return (iMp3FreeRes ?: iRes)?.searchMp3(name, page) ?: emptyList()
        } else {
            return (iRes ?: iMp3FreeRes)?.searchMp3(name, page, TypeProxy.name) ?: emptyList()
        }

    }
}

var TypeProxy = Type.netease

enum class Type(var title: String, var value: String) {
    mp3free("mp3Free", ""),
    netease("网易", "netease"), qq("QQ", "qq"),
    kugou("酷狗", "kugou"), kuwo("酷我", "kuwo"),
    baidu("百度", "baidu"), yting("一听", "1ting"), migu("咪咕", "migu"),
    lizhi("荔枝", "lizhi"), qingting("蜻蜓", "qingting"),
    ximalaya("喜马拉雅", "ximalaya"), wusingyc("5sing原创", "5singyc"),
    wuxingfc("5sing翻唱", "5singfc")
}