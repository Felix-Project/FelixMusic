package com.felix.music.core

import android.content.Context
import com.felix.utils.BaseUtilsApp

open class BaseCompApp : BaseUtilsApp() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        appConfig.forEach {
            it.onCreate(this, "")
        }
    }
}