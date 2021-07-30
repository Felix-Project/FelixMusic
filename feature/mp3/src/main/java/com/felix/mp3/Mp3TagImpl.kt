package com.felix.mp3

import android.util.Log
import com.felix.resp.IMp3Tag
import com.felix.utils.utils.ITAG
import java.io.File

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.mp3
 * @ClassName: Mp3Impl
 * @Author: 80341341
 * @CreateDate: 2021/7/29 16:42
 * @Description: Mp3Impl类作用描述
 */
class Mp3TagImpl : IMp3Tag, ITAG {
    override fun writeID3V24(file: File, iD3Tag: IMp3Tag.ID3Tag): Boolean {
        if (!file.exists()) {
            Log.i(TAG, "writeID3V24: file(${file.absolutePath}) not exists.")
            return false
        }
        if (!file.canRead()) {
            Log.i(TAG, "writeID3V24: file(${file.absolutePath}) can't no be read.")
            return false
        }
        val mp3File = Mp3File(file)
        mp3File.removeCustomTag()
        mp3File.removeId3v1Tag()
        mp3File.removeId3v2Tag()
        mp3File.id3v2Tag = ID3v24Tag().apply {
            title = iD3Tag.title
            album = iD3Tag.album
            artist = iD3Tag.artist
            setAlbumImage(iD3Tag.albumImage, iD3Tag.mimeType)
        }
        val tmpFile = File(file.parent, file.name.plus(".tmp"))
        try {
            mp3File.save(tmpFile.absolutePath)
            file.delete()
            tmpFile.renameTo(file)
            return true
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return false
    }
}