package com.felix.download

import java.io.File

interface IDownload {
    data class DownloadConfig(var url: String, var name: String, var directory: File? = null)

    fun download(
        downloadConfig: DownloadConfig,
        onCompelete: (file: File) -> Unit,
        onError: (Throwable) -> Unit = { it.printStackTrace() },
        onProgress: ((downloaded: Long, total: Long) -> Unit)? = null
    )

    data class BitmapHolder(var byteArray: ByteArray, var contentType: String)

    fun downloadBitmap(url: String): BitmapHolder?
}

object DownloadProxy : IDownload {
    var iDownload: IDownload? = null
    override fun download(
        downloadConfig: IDownload.DownloadConfig,
        onCompelete: (file: File) -> Unit,
        onError: (Throwable) -> Unit,
        onProgress: ((downloaded: Long, total: Long) -> Unit)?
    ) {
        iDownload?.download(downloadConfig, onCompelete, onError, onProgress) ?: kotlin.run {
            onError.invoke(NullPointerException("not implemetation for iDownload."))
        }
    }

    override fun downloadBitmap(url: String): IDownload.BitmapHolder? {
        return iDownload?.downloadBitmap(url)
    }
}