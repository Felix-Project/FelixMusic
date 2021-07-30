package com.felix.resp

import android.content.Context
import com.felix.music.core.IAppInit
import com.felix.utils.BaseUtilsApp

class RespApp : BaseUtilsApp() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        RespAppInit().onCreate(this, "")
        (Class.forName("com.felix.download.DownloadAppInit").newInstance() as IAppInit).onCreate(
            this,
            ""
        )
    }
}