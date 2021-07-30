package com.felix.search

import android.app.Application
import com.felix.music.core.IAppInit

class SearchAppInit : IAppInit {
    override fun onCreate(app: Application, flavor: String, vararg args: String) {
        SearchProxy.iSearch = SearchImpl()
    }
}