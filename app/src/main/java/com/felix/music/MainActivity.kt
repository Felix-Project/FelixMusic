package com.felix.music

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.felix.id3tool.ID3ToolProxy
import com.felix.music.databinding.ActivityMainBinding
import com.felix.search.SearchProxy
import androidx.appcompat.app.ActionBarDrawerToggle


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater).apply {
            vpMain.adapter = MainPager(supportFragmentManager)

            val toggle = ActionBarDrawerToggle(
                this@MainActivity, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            toggle.syncState()
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
}