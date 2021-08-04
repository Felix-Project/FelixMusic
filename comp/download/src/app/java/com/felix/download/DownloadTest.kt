package com.felix.download

import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Field

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.download
 * @ClassName: DownloadTest
 * @Author: 80341341
 * @CreateDate: 2021/8/4 14:36
 * @Description: DownloadTest 类作用描述
 */
class DownloadTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            download(
                "http://music.163.com/song/media/outer/url?id=167876.mp3",
                File("许嵩 - 有何不可.mp3")
            )
        }

        val client: OkHttpClient by lazy {
            OkHttpClient()
        }

        fun download(url: String, file: File) {
            val request: Request = Request.Builder().url(url)
                .header(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36"
                )
                .header(
                    "Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
                )

                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    var `is`: InputStream? = null
                    val buf = ByteArray(2048)
                    var len = 0
                    var fos: FileOutputStream? = null
                    try {
                        `is` = response.body?.byteStream()
                        val total: Long = response.body?.contentLength() ?: 0
                        if (file.exists()) {
//                        if (file.length() == total) {
//                            //无需下载
//                            onCompelete.invoke(savePath)
//                            return
//                        }
                            file.delete()
                        }
                        println(total)
                        println(file.absolutePath)
                        file.createNewFile()
                        fos = FileOutputStream(file)
                        var sum: Long = 0
                        while ((`is`?.read(buf) ?: -1).also { len = it } != -1) {
                            fos?.write(buf, 0, len)
                            sum += len.toLong()
                            if (len >= 0) {
                                println("len is $len ,content=" + String(buf, 0, len))
                            } else {
                                println("len is $len")
                            }
                        }
                        fos?.flush()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            `is`?.close()
                        } catch (e: IOException) {
                        }
                        try {
                            fos?.close()
                        } catch (e: IOException) {
                        }
                    }
                }
            })
        }
    }
}