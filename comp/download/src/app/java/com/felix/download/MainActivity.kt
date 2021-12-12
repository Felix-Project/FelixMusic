package com.felix.download

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import com.felix.lib_app_tools.toast.ToastProxy
import com.felix.utils.handler.UIProxy
import com.felix.utils.utils.ITAG
import java.io.File

class MainActivity : AppCompatActivity(), ITAG {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 30) {
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {

                //跳转到设置界面引导用户打开
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + this.packageName)
                startActivityForResult(intent, 3)
            }
        }
        findViewById<View>(R.id.tvTest).setOnClickListener {
            download("http://music.163.com/song/media/outer/url?id=167876.mp3", "许嵩 - 有何不可.mp3")
//        download("http://m10.music.126.net/20210804152441/897ee032c909e19f8fa72ab9298e94b5/ymusic/a024/09a7/c4c3/fc0d416790bc729172c636e2d2d1109a.mp3", "许嵩 - 有何不可.mp3")
        }
    }

    fun download(url: String, name: String) {
        Log.i(TAG, "download: $url,$name")
        IDownload.DownloadConfig(
            url = url,
            name = name,
            File(Environment.getExternalStorageDirectory(), "felix/Music/Download-tmp")
        ).let { config ->
            DownloadProxy.download(config, { file ->
                Log.d(TAG, "onCompelete: download success.")
            }, {
                Log.e(TAG, "onError: ", it)

            }, { downloaded, total ->
                Log.i(TAG, "onProgress: downloaded=$downloaded,total=$total")
            })
        }
    }
}