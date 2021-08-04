package com.felix.resp

import android.app.Application
import com.felix.music.core.IAppInit

class RespAppInit : IAppInit {
    override fun onCreate(app: Application, flavor: String, vararg args: String) {
        ResProxy.iMp3FreeRes = Mp3FreeImpl()
        ResProxy.iRes = NeteaseImpl()
        ResProxy.iCacheRes = DbCacheRes()
    }
}