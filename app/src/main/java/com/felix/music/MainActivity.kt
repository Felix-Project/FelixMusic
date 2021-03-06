package com.felix.music

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.felix.arch.mvvm.IBackable
import com.felix.id3tool.ID3ToolProxy
import com.felix.lib_arch.mvvm.BaseActivity
import com.felix.music.databinding.ActivityMainBinding
import com.felix.search.SearchProxy


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            vpMain.adapter = MainPager(supportFragmentManager)
            setContentView(root)
        }
        PermissionUtils.request(this)
    }

    class MainPager(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        val fragmentList = arrayOf(
            SearchProxy.newFragment(),
            ID3ToolProxy.newFragment()
        )

        override fun getCount() = fragmentList.size

        override fun getItem(position: Int) = fragmentList[position]
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}