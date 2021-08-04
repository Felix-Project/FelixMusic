package com.felix.download

import android.app.Application
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import com.felix.download.base.AbsDownload
import java.io.File

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.download
 * @ClassName: DownloadManagerImpl
 * @Author: 80341341
 * @CreateDate: 2021/8/4 14:15
 * @Description: DownloadManagerImpl 类作用描述
 */
class DownloadManagerImpl(var app: Application) : AbsDownload(app) {
    override fun download(
        downloadConfig: IDownload.DownloadConfig,
        onCompelete: (file: File) -> Unit,
        onError: (Throwable) -> Unit,
        onProgress: ((downloaded: Long, total: Long) -> Unit)?
    ) {
        val mDownloadManager = app.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val resource = Uri.parse(downloadConfig.url)

        val request = DownloadManager.Request(resource)
        //下载的本地路径，表示设置下载地址为SD卡的Download文件夹，文件名为mobileqq_android.apk。
        request.setDestinationInExternalPublicDir("Download", downloadConfig.name)

        //start 一些非必要的设置
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setVisibleInDownloadsUi(true)
        request.setTitle(downloadConfig.name)
        //end 一些非必要的设置

        mDownloadManager.enqueue(request)
    }

    override fun downloadBitmap(url: String): IDownload.BitmapHolder? {
        return null
    }
}