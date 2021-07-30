package com.felix.download

import android.app.Application
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import java.io.File

class DownloadImpl(var app: Application) : IDownload {
    override fun download(
        downloadConfig: IDownload.DownloadConfig,
        onCompelete: (file: File) -> Unit,
        onError: (Throwable) -> Unit,
        onProgress: ((downloaded: Long, total: Long) -> Unit)?
    ) {
        val listener = object : FileDownloadListener() {
            override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

            }

            override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                onProgress?.invoke(soFarBytes.toLong(), totalBytes.toLong())
            }

            override fun completed(task: BaseDownloadTask?) {
                task?.targetFilePath?.let {
                    File(it)
                }?.let(onCompelete) ?: kotlin.run {
                    onError.invoke(NullPointerException("file is null."))
                }
            }

            override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
            }

            override fun error(task: BaseDownloadTask?, e: Throwable?) {
                onError.invoke(e ?: NullPointerException("Throwable is null."))
            }

            override fun warn(task: BaseDownloadTask?) {
            }

        }
        FileDownloader.getImpl().create(downloadConfig.url)
            .setPath(
                File(
                    downloadConfig.directory ?: app.getExternalFilesDir("Download"),
                    downloadConfig.name
                ).absolutePath
            )
            .setListener(listener)
            .start()
    }

    override fun downloadBitmap(url: String): IDownload.BitmapHolder? {
        return null
    }
}