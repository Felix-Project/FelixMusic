package com.felix.search

import androidx.fragment.app.Fragment

interface ISearch {
    fun newFragment(): Fragment
}

object SearchProxy : ISearch {
    var iSearch: ISearch? = null
    override fun newFragment(): Fragment {
        return iSearch?.newFragment() ?: Fragment()
    }
}