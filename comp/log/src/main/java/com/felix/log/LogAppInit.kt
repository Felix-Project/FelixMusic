package com.felix.log

import android.app.Application
import com.felix.music.core.IAppInit
import com.felix.utils.utils.ITAG

class LogAppInit : IAppInit, ITAG {
    override fun onCreate(app: Application, flavor: String, vararg args: String) {
        FLog.iLog = LogImpl()
    }
}