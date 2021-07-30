package com.felix.music.core

import android.app.Application

interface IAppInit {
    fun onCreate(app: Application, flavor: String, vararg args: String)
}