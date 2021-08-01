package com.felix.id3tool

import androidx.fragment.app.Fragment

interface IID3Tool {
    fun newFragment(): Fragment
}

object ID3ToolProxy : IID3Tool {
    var iiD3Tool: IID3Tool? = null
    override fun newFragment(): Fragment {
        return iiD3Tool?.newFragment() ?: Fragment()
    }
}