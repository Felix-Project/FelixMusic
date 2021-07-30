package com.felix.resp.utils

import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.RandomAccessFile
import java.nio.charset.Charset

private const val TAG = "ID3Utils"
fun File.writeID3V2Test(
    songName: String?,
    artistName: String?, albumName: String?
) {
    println("${TAG}.writeID3V2: write songName=$songName,artist=$artistName,album=$albumName")
    var musicRandomAccessFile: RandomAccessFile? = null
    try {
        musicRandomAccessFile = RandomAccessFile(this, "rw")
        musicRandomAccessFile?.seek(0)
        val tag = ByteArray(3)
        musicRandomAccessFile?.read(tag)
        if (String(tag) == "ID3") {
            println("${TAG}.writeID3V2: file has ID3,write overide")
        }
    } catch (e: java.lang.Exception) {
        System.err.println("${TAG}.存储音乐文件异常.${e.message}")
    }
    try {
        val encodeByte = byteArrayOf(3) // 03 表示的UTF8编码
        val tagByteArray: ByteArray
        val tagHeadByteArray: ByteArray
        var tagFrameHeadByteArray: ByteArray
        val songNameByteArray = (songName ?: "").toByteArray(charset("UTF-8"))
        val artistNameByteArray = (artistName ?: "").toByteArray(charset("UTF-8"))
        val albumNameByteArray = (albumName ?: "").toByteArray(charset("UTF-8"))
        val tagHeadLength = 10
        val tagFrameHeadLength = 10
        val tagFrameEncodeLength = 1
        val tagFillByteLength = 20 // 这个填充字节是我看到其他MP3文件ID3标签都会在尾端添加的数据，为了保险起见我也加上了
        var byteArrayOffset = 0
        val songNameByteArrayLength = songNameByteArray.size
        val artistNameByteArrayLength = artistNameByteArray.size
        val albumNameByteArrayLength = albumNameByteArray.size
        val songNameFrameTotalLength = songNameByteArrayLength + tagFrameEncodeLength
        val artistNameFrameTotalLength = artistNameByteArrayLength + tagFrameEncodeLength
        val albumNameFrameTotalLength = albumNameByteArrayLength + tagFrameEncodeLength
        val totalTagLength = tagHeadLength + tagFrameHeadLength + songNameByteArrayLength +
                tagFrameHeadLength + artistNameByteArrayLength +
                tagFrameHeadLength + albumNameByteArrayLength +
                tagFillByteLength
        val tagContentLength = totalTagLength - tagHeadLength
        tagByteArray = ByteArray(totalTagLength)
        tagHeadByteArray = ByteArray(tagHeadLength)
        System.arraycopy("ID3".toByteArray(), 0, tagHeadByteArray, 0, 3)
        tagHeadByteArray[3] = 3
        tagHeadByteArray[4] = 0
        tagHeadByteArray[5] = 0
        tagHeadByteArray[6] = ((tagContentLength shr 7 shr 7 shr 7) % 128).toByte()
        tagHeadByteArray[7] = ((tagContentLength shr 7 shr 7) % 128).toByte()
        tagHeadByteArray[8] = ((tagContentLength shr 7) % 128).toByte()
        tagHeadByteArray[9] = (tagContentLength % 128).toByte()
        System.arraycopy(
            tagHeadByteArray, 0, tagByteArray, byteArrayOffset,
            tagHeadLength
        )
        byteArrayOffset += tagHeadLength
        tagFrameHeadByteArray = ByteArray(tagFrameHeadLength)
        System.arraycopy("TIT2".toByteArray(), 0, tagFrameHeadByteArray, 0, 4)
        tagFrameHeadByteArray[4] = ((songNameFrameTotalLength shr 8 shr 8 shr 8) % 256).toByte()
        tagFrameHeadByteArray[5] = ((songNameFrameTotalLength shr 8 shr 8) % 256).toByte()
        tagFrameHeadByteArray[6] = ((songNameFrameTotalLength shr 8) % 256).toByte()
        tagFrameHeadByteArray[7] = (songNameFrameTotalLength % 256).toByte()
        tagFrameHeadByteArray[8] = 0
        tagFrameHeadByteArray[9] = 0
        System.arraycopy(
            tagFrameHeadByteArray,
            0,
            tagByteArray,
            byteArrayOffset,
            tagFrameHeadLength
        )
        byteArrayOffset += tagFrameHeadLength
        System.arraycopy(encodeByte, 0, tagByteArray, byteArrayOffset, tagFrameEncodeLength)
        byteArrayOffset += tagFrameEncodeLength
        System.arraycopy(
            songNameByteArray, 0, tagByteArray, byteArrayOffset,
            songNameByteArrayLength
        )
        byteArrayOffset += songNameByteArrayLength
        tagFrameHeadByteArray = ByteArray(tagFrameHeadLength)
        System.arraycopy("TPE1".toByteArray(), 0, tagFrameHeadByteArray, 0, 4)
        tagFrameHeadByteArray[4] =
            ((artistNameFrameTotalLength shr 8 shr 8 shr 8) % 256).toByte()
        tagFrameHeadByteArray[5] = ((artistNameFrameTotalLength shr 8 shr 8) % 256).toByte()
        tagFrameHeadByteArray[6] = ((artistNameFrameTotalLength shr 8) % 256).toByte()
        tagFrameHeadByteArray[7] = (artistNameFrameTotalLength % 256).toByte()
        tagFrameHeadByteArray[8] = 0
        tagFrameHeadByteArray[9] = 0
        System.arraycopy(
            tagFrameHeadByteArray,
            0,
            tagByteArray,
            byteArrayOffset,
            tagFrameHeadLength
        )
        byteArrayOffset += tagFrameHeadLength
        System.arraycopy(encodeByte, 0, tagByteArray, byteArrayOffset, tagFrameEncodeLength)
        byteArrayOffset += tagFrameEncodeLength
        System.arraycopy(
            artistNameByteArray, 0, tagByteArray, byteArrayOffset,
            artistNameByteArrayLength
        )
        byteArrayOffset += artistNameByteArrayLength
        tagFrameHeadByteArray = ByteArray(tagFrameHeadLength)
        System.arraycopy("TALB".toByteArray(), 0, tagFrameHeadByteArray, 0, 4)
        tagFrameHeadByteArray[4] =
            ((albumNameFrameTotalLength shr 8 shr 8 shr 8) % 256).toByte()
        tagFrameHeadByteArray[5] = ((albumNameFrameTotalLength shr 8 shr 8) % 256).toByte()
        tagFrameHeadByteArray[6] = ((albumNameFrameTotalLength shr 8) % 256).toByte()
        tagFrameHeadByteArray[7] = (albumNameFrameTotalLength % 256).toByte()
        tagFrameHeadByteArray[8] = 0
        tagFrameHeadByteArray[9] = 0
        System.arraycopy(
            tagFrameHeadByteArray,
            0,
            tagByteArray,
            byteArrayOffset,
            tagFrameHeadLength
        )
        byteArrayOffset += tagFrameHeadLength
        System.arraycopy(encodeByte, 0, tagByteArray, byteArrayOffset, tagFrameEncodeLength)
        byteArrayOffset += tagFrameEncodeLength
        System.arraycopy(
            albumNameByteArray, 0, tagByteArray, byteArrayOffset,
            albumNameByteArrayLength
        )
        musicRandomAccessFile?.write(tagByteArray)
//        outputStream().write(tagByteArray)
        println("${TAG}.writeID3V2: write success.")
    } catch (e: java.lang.Exception) {
        System.err.println("${TAG}.写入音乐标签异常.${e.message}")
    }
}

private inline fun newFrame(frameTag: String, content: String?): ByteArray {
    return newFrame(frameTag, (content ?: "").toByteArray(Charset.forName("unicode")))
}

private inline fun newFrame(frameTag: String, contentArray: ByteArray): ByteArray {
    val tagFrameHeadLength = 10
    val songSize = contentArray.size
    //frameTag必须为4个字节
    assert(frameTag.length == 4)
    //ByteArray至少一个字节
    assert(contentArray.size > 0)
    val frame = ByteArray(tagFrameHeadLength + songSize)
    System.arraycopy(frameTag.toByteArray(), 0, frame, 0, 4)
    System.arraycopy(songSize.toByteArray(), 0, frame, 4, 4)
    frame[8] = 0
    frame[9] = 0
    System.arraycopy(contentArray, 0, frame, 10, songSize)
    return frame
}

fun File.writeID3V2(
    songName: String?,
    artistName: String?, albumName: String?
) {
    println("${TAG}.writeID3V2: write songName=$songName,artist=$artistName,album=$albumName")


    //=========frame=========
    data class TagHolder(var tag: String, var content: String?)

    val frameList = arrayOf(
        TagHolder("TIT2", songName),
        TagHolder("TPE1", artistName),
        TagHolder("TALB", albumName)
    ).map {
        newFrame(it.tag, it.content)
    }

    //=========header tag=========
    val headerArray = ByteArray(10)
    //header，3个字节，ID3V2.3标识符"ID3"的Ascii码，否则认为没有ID3V2.3
    System.arraycopy("ID3".toByteArray(), 0, headerArray, 0, 3)
    headerArray[3] = 3      // 版本号，＝03
    headerArray[4] = 0      // 副版本号，＝00
    headerArray[5] = 0      // 标志字节，一般没意义，＝00
    //4个字节，标签内容长度，高位在前，不包括标签头的10个字节
    val tagHeadLength = 10
    val totalTagLength = tagHeadLength + frameList.map { it.size }.sum()
    System.arraycopy(totalTagLength.toByteArray(), 0, headerArray, 6, 4)

    this.runCatching {
        val randomAccessFile = RandomAccessFile(this, "rw")
        randomAccessFile.seek(0)
        val tag = ByteArray(3)
        randomAccessFile.read(tag)
        if (String(tag) != "ID3") {
            println("${TAG}.writeID3V2: file has ID3,write overide")
            randomAccessFile.seek(0)
            randomAccessFile.write(headerArray)
            frameList.forEach {
                randomAccessFile.write(it)
            }
            randomAccessFile.close()
            return
        } else {
            //创建tmp文件，跳过ID3v2标签
            val tmpFile = File(this.absolutePath.plus(".mp3")).also {
                if (it.exists()) {
                    it.delete()
                }
                it.createNewFile()
            }
            tmpFile.outputStream().sink().buffer().let { sink ->
                //写入ID3V2
                sink.write(headerArray)
                frameList.forEach {
                    sink.write(it)
                }

                //写入mp3部分文件
                val sizeTag = ByteArray(4)
                randomAccessFile.seek(6)
                randomAccessFile.read(sizeTag)
                randomAccessFile.seek(sizeTag.toInt().toUInt().toLong())
                val buffer = ByteArray(1024)
                var size: Int
                while (randomAccessFile.read(buffer).also {
                        size = it
                    } != -1) {
                    sink.write(buffer, 0, size)
                }
                sink.flush()
                sink.close()
                randomAccessFile.close()
            }
            this.delete()
            tmpFile.renameTo(this)
        }
    }.exceptionOrNull()?.printStackTrace()
}
