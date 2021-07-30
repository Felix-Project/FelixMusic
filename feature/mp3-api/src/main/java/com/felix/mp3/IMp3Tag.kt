package com.felix.resp

import java.io.File

interface IMp3Tag {
    data class ID3Tag(var title: String, var artist: String, var album: String)

    fun writeID3V24(file: File, iD3Tag: ID3Tag): Boolean
}

object Mp3TagProxy : IMp3Tag {
    var iMp3Tag: IMp3Tag? = null
    override fun writeID3V24(file: File, iD3TagTag: IMp3Tag.ID3Tag): Boolean {
        return iMp3Tag?.writeID3V24(file, iD3TagTag) ?: false
    }
}