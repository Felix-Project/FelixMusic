package com.felix.download

import android.app.Application

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.download
 * @ClassName: DownloadApp
 * @Author: 80341341
 * @CreateDate: 2021/8/4 14:45
 * @Description: DownloadApp 类作用描述
 */
class DownloadApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DownloadAppInit().onCreate(this, "")
    }
}