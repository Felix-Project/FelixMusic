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
    album = "",
    albumImageThumb = pic,
    albumImage = pic,
    albumImageOrigin = pic
)