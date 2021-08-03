package com.felix.resp

data class Mp3FreeResp(
    val response: List<SongBean>?
)

data class SongBean(
    val access_key: String?,
    val album: Album?,
    val album_id: Int?,
    val artist: String?,
    val date: Int?,
    val duration: Int?,
    val genre_id: Int?,
    val id: Int?,
    val is_licensed: Boolean?,
    val owner_id: Int?,
    val short_videos_allowed: Boolean?,
    val stories_allowed: Boolean?,
    val stories_cover_allowed: Boolean?,
    val title: String?,
    val url: String?
)

data class Album(
    val access_key: String?,
    val id: Int?,
    val owner_id: Int?,
    val thumb: Thumb?,
    val title: String?
)

data class Thumb(
    val height: Int?,
    val photo_1200: String?,
    val photo_135: String?,
    val photo_270: String?,
    val photo_300: String?,
    val photo_34: String?,
    val photo_600: String?,
    val photo_68: String?,
    val width: Int?
)

fun SongBean.toMp3Bean() = Mp3Bean(
    title = title ?: "",
    artist = artist ?: "",
    album = album?.title ?: "",
    url = url ?: "",
    albumImageThumb = album?.thumb?.photo_135,
    albumImage = album?.thumb?.photo_300,
    albumImageOrigin = album?.thumb?.photo_1200
)
