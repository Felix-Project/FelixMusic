package com.felix.music.core

val appConfig = arrayOf(
    "com.felix.log.LogAppInit",
    "com.felix.download.DownloadAppInit",
    "com.felix.resp.RespAppInit",
    "com.felix.search.SearchAppInit",
    "com.felix.mp3.Mp3AppInit"
).mapNotNull {
    it.runCatching {
        Class.forName(this).newInstance()
    }.also {
        it.exceptionOrNull()?.printStackTrace()
    }.getOrNull()?.takeIf { it is IAppInit }?.let {
        it as IAppInit
    }
}