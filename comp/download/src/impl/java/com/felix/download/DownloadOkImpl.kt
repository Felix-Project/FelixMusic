package com.felix.download

import android.app.Application
import com.felix.download.base.AbsDownload
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class DownloadOkImpl(app: Application) : AbsDownload(app) {
    val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    override fun download(
        downloadConfig: IDownload.DownloadConfig,
        onCompelete: (file: File) -> Unit,
        onError: (Throwable) -> Unit,
        onProgress: ((downloaded: Long, total: Long) -> Unit)?
    ) {

        val request: Request = Request.Builder().url(downloadConfig.url)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36"
            )
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError.invoke(e)
            }

            override fun onResponse(call: Call, response: Response) {
                var `is`: InputStream? = null
                val buf = ByteArray(2048)
                var len = 0
                var fos: FileOutputStream? = null
                try {
                    `is` = response.body?.byteStream()
                    val total: Long = response.body?.contentLength() ?: 0
                    val file = downloadConfig.getSaveFile()
                    if (file.exists()) {
//                        if (file.length() == total) {
//                            //无需下载
//                            onCompelete.invoke(savePath)
//                            return
//                        }
                        file.delete()
                    }
                    println(file.absolutePath)
                    file.createNewFile()
                    fos = FileOutputStream(file)
                    var sum: Long = 0
                    while ((`is`?.read(buf) ?: -1).also { len = it } != -1) {
                        fos?.write(buf, 0, len)
                        sum += len.toLong()
                        if (debug) {
                            if (len >= 0) {
                                println("len is $len ,content=" + String(buf, 0, len))
                            } else {
                                println("len is $len")
                            }
                        }
                        onProgress?.invoke(sum, total)
                    }
                    fos?.flush()
                    // 下载完成
                    onCompelete.invoke(file)
                } catch (e: Exception) {
                    onError.invoke(e)
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

    override fun downloadBitmap(url: String): IDownload.BitmapHolder? {
        val request: Request = Request.Builder().url(url).build()
        try {
            val response = client.newCall(request).execute()
            val type = response.headers["content-type"] ?: "image/jpeg"
            return response.body?.bytes()?.let {
                IDownload.BitmapHolder(it, type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}