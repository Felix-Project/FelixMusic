package com.felix.mp3

import android.app.Application
import com.felix.music.core.IAppInit
import com.felix.resp.Mp3TagProxy

class Mp3AppInit : IAppInit {
    override fun onCreate(app: Application, flavor: String, vararg args: String) {
        Mp3TagProxy.iMp3Tag = Mp3TagImpl()
    }
}