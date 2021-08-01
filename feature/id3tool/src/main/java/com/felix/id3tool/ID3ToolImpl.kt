package com.felix.id3tool

import androidx.fragment.app.Fragment

class ID3ToolImpl : IID3Tool {
    override fun newFragment(): Fragment {
        return Id3ToolFragment()
    }
}