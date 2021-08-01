package com.felix.id3tool

import android.app.Application
import com.felix.music.core.IAppInit

class ID3ToolAppInit : IAppInit {
    override fun onCreate(app: Application, flavor: String, vararg args: String) {
        ID3ToolProxy.iiD3Tool = ID3ToolImpl()
    }
}