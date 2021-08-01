package com.felix.music

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.felix.id3tool.ID3ToolProxy
import com.felix.search.SearchProxy

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQ_PERMISSON_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ViewPager>(R.id.vpMain).apply {
            adapter = MainPager(supportFragmentManager)
        }

//        supportFragmentManager.beginTransaction().let {
//            it.add(R.id.container, ID3ToolProxy.newFragment())
//        }.commit()
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

    fun requestPermission() {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).filter {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        }.takeIf {
            it.isNotEmpty()
        }?.toTypedArray()?.let {
            ActivityCompat.requestPermissions(this, it, REQ_PERMISSON_CODE)
        } ?: kotlin.run {
            if (Build.VERSION.SDK_INT >= 30) {
                // 先判断有没有权限
                if (!Environment.isExternalStorageManager()) {

                    //跳转到设置界面引导用户打开
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:" + this.packageName)
                    startActivityForResult(intent, 3)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestPermission()
    }
}