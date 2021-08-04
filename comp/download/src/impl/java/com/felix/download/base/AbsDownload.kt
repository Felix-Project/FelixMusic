package com.felix.download.base

import android.content.Context
import com.felix.download.IDownload
import java.io.File

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.download
 * @ClassName: AbsDownload
 * @Author: 80341341
 * @CreateDate: 2021/8/4 14:19
 * @Description: AbsDownload 类作用描述
 */
abstract class AbsDownload(var context: Context) : IDownload {
    val debug = false
    fun IDownload.DownloadConfig.getSaveFile(): File {
        // 储存下载文件的目录
        val parent = this.directory ?: context.getExternalFilesDir("Download")
        println("exist=${parent?.exists()}")
        parent?.takeIf { !it.exists() }?.let {
            println("make dirs.")
            it.mkdirs()
        }

        return File(parent, name)
    }
}