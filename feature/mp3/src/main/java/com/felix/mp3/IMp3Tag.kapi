package com.felix.resp

import java.io.File

interface IMp3Tag {
    data class ID3Tag(
        var title: String,
        var artist: String,
        var album: String,
        var albumImage: ByteArray? = null,
        var mimeType: String = ""
    )

    fun writeID3V24(file: File, iD3Tag: ID3Tag): Boolean
    fun fillID3V24(file: File, iD3Tag: ID3Tag): Boolean
    fun getID3V24(file: File): ID3Tag
}

object Mp3TagProxy : IMp3Tag {
    var iMp3Tag: IMp3Tag? = null
    override fun writeID3V24(file: File, iD3TagTag: IMp3Tag.ID3Tag): Boolean {
        return iMp3Tag?.writeID3V24(file, iD3TagTag) ?: false
    }

    override fun fillID3V24(file: File, iD3Tag: IMp3Tag.ID3Tag): Boolean {
        return iMp3Tag?.fillID3V24(file, iD3Tag) ?: false
    }

    override fun getID3V24(file: File): IMp3Tag.ID3Tag {
        return iMp3Tag?.getID3V24(file) ?: IMp3Tag.ID3Tag("", "", "")
    }
}