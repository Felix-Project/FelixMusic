package com.felix.music.core

import com.felix.utils.BaseUtilsApp

open class BaseCompApp : BaseUtilsApp() {
    override fun onCreate() {
        super.onCreate()
        appConfig.forEach {
            it.onCreate(this, "")
        }
    }
}