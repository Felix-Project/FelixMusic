package com.felix.download

import android.app.Application
import com.felix.music.core.IAppInit
import com.liulishuo.filedownloader.FileDownloader

class DownloadAppInit : IAppInit {
    override fun onCreate(app: Application, flavor: String, vararg args: String) {
        FileDownloader.setup(app)
        DownloadProxy.iDownload = DownloadOkImpl(app)
    }
}