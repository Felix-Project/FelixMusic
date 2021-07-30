package com.felix.resp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.felix.download.DownloadProxy
import com.felix.download.IDownload
import com.felix.lib_app_tools.toast.ToastDelegate
import com.felix.resp.utils.writeID3V2
import com.felix.utils.gson.toJson
import com.felix.utils.utils.ITAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.RandomAccessFile

class MainActivity : Activity(), ITAG {
    companion object {
        const val REQ_PERMISSON_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.tvTest).setOnClickListener {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).filter {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            }.takeIf {
                it.isNotEmpty()
            }?.toTypedArray()?.let {
                ActivityCompat.requestPermissions(this, it, REQ_PERMISSON_CODE)
            } ?: kotlin.run {
                if (Build.VERSION.SDK_INT >= 30) {
                    // 先判断有没有权限
                    if (Environment.isExternalStorageManager()) {
//                        testClick()
//                        testID3()
                        TestID3.main(this)

                    } else {
                        //跳转到设置界面引导用户打开
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.data = Uri.parse("package:" + this.packageName)
                        startActivityForResult(intent, 3)
                    }
                }
            }
        }
    }

    fun testClick() {
        GlobalScope.launch(Dispatchers.IO) {
            ResProxy.searchMp3("有何不可", 0).let {
                Log.i("TAG", "onCreate: ${it.toJson()}")
                it.firstOrNull()
            }?.let { songBean ->
                IDownload.DownloadConfig(
                    url = songBean.url ?: "",
                    name = "${songBean.title} - ${songBean.artist}",
                    File(Environment.getExternalStorageDirectory(), "aaaDownload")
                ).let {
                    DownloadProxy.download(it, {
                        it.writeID3V2(songBean.title, songBean.artist, songBean.album?.title)
                        launch(Dispatchers.Main) {
                            ToastDelegate.show(it.absolutePath)
                        }
                    }, {
                        Log.e(TAG, "testClick: ", it)
                    }, { downloaded, total ->
                        Log.i(TAG, "testClick: downloaded=$downloaded,total=$total")
                    })
                }
            }
        }
    }

    fun testID3() {
        val parent = File(Environment.getExternalStorageDirectory(), "aaaDownload")
        if (!parent.exists()) {
            parent.mkdirs()
        }
        val backFile = File(parent, "backup.mp3")
        val newFile = File(parent, "test.mp3")
        newFile.also {
            if (it.exists()) {
                it.delete()
            }
        }.also {
            it.createNewFile()
        }.also {
            it.outputStream().sink().buffer().writeAll(backFile.inputStream().source())
        }.let {
            it.writeID3V2("test", "testartist", "test album")
        }

    }

    fun StorageMusicFileWithID3V1Tag(
        musicFilePath: String?,
        songName: String, artistName: String,
        albumName: String
    ) {
        try {
            val musicRandomAccessFile = RandomAccessFile(musicFilePath, "rw")
            musicRandomAccessFile.seek(musicRandomAccessFile.length() - 128) // 跳到ID3V1开始的位置
            val tag = ByteArray(3)
            musicRandomAccessFile.read(tag)
            if (String(tag) == "TAG") {
                return
            }
            val tagByteArray = ByteArray(128)
            musicRandomAccessFile.seek(musicRandomAccessFile.length())
            val songNameByteArray = songName.toByteArray(charset("GBK"))
            val artistNameByteArray = artistName.toByteArray(charset("GBK"))
            val albumNameByteArray = albumName.toByteArray(charset("GBK"))
            var songNameByteArrayLength = songNameByteArray.size
            var artistNameByteArrayLength = artistNameByteArray.size
            var albumNameByteArrayLength = albumNameByteArray.size
            songNameByteArrayLength =
                if (songNameByteArrayLength > 30) 30 else songNameByteArrayLength
            artistNameByteArrayLength =
                if (artistNameByteArrayLength > 30) 30 else artistNameByteArrayLength
            albumNameByteArrayLength =
                if (albumNameByteArrayLength > 30) 30 else albumNameByteArrayLength
            System.arraycopy("TAG".toByteArray(), 0, tagByteArray, 0, 3)
            System.arraycopy(songNameByteArray, 0, tagByteArray, 3, songNameByteArrayLength)
            System.arraycopy(artistNameByteArray, 0, tagByteArray, 33, artistNameByteArrayLength)
            System.arraycopy(albumNameByteArray, 0, tagByteArray, 63, albumNameByteArrayLength)
            tagByteArray[127] = 0xFF.toByte() // 将流派显示为指定音乐的流派
            musicRandomAccessFile.write(tagByteArray)
        } catch (e: Exception) {
            Log.w(TAG, "StorageMusicFileWithID3V1Tag: 写入音乐标签异常", e)
        }
    }

    fun StorageMusicFileWithID3V2Tag(
        sourceFile: File, songName: String,
        artistName: String, albumName: String
    ) {
        try {
            val musicRandomAccessFile = RandomAccessFile(sourceFile.absolutePath, "rw")
            musicRandomAccessFile.seek(0)
            val tag = ByteArray(3)
            musicRandomAccessFile.read(tag)
            if (String(tag) == "ID3") {
                return
            }
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "存储音乐文件异常", e)
        }
        try {
            val encodeByte = byteArrayOf(3) // 03 表示的UTF8编码
            val tagByteArray: ByteArray
            val tagHeadByteArray: ByteArray
            var tagFrameHeadByteArray: ByteArray
            val songNameByteArray = songName.toByteArray(charset("UTF-8"))
            val artistNameByteArray = artistName.toByteArray(charset("UTF-8"))
            val albumNameByteArray = albumName.toByteArray(charset("UTF-8"))
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
            sourceFile.outputStream().write(tagByteArray)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "写入音乐标签异常", e)
        }
    }
}