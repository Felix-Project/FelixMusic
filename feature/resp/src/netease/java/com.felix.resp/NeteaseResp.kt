package com.felix.resp

data class NeteaseResp(
    val code: Int,
    val data: List<Data>?,
    val error: String?
)

data class Data(
    val author: String?,
    val link: String?,
    val lrc: String?,
    val pic: String?,
    val songid: Int?,
    val title: String?,
    val type: String?,
    val url: String?
)

fun Data.toMp3Bean() = Mp3Bean(
    title = title ?: "",
    artist = author ?: "",
    album = title ?: "",
    url = url ?: "",
    albumImageThumb = pic?.let {
        return@let if (it.endsWith("300x300") || it.endsWith("300X300")) {
            it.substring(0, it.length - 7).plus("144x144")
        } else {
            it
        }
    },
    albumImage = pic,
    albumImageOrigin = pic?.let {
        return@let if (it.endsWith("300x300") || it.endsWith("300X300")) {
            it.substring(0, it.length - 7)
        } else {
            it
        }
    }
)