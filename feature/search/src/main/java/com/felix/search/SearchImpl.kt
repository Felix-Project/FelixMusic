package com.felix.search

import androidx.fragment.app.Fragment

class SearchImpl : ISearch {
    override fun newFragment(): Fragment {
        return SearchFragment()
    }
}