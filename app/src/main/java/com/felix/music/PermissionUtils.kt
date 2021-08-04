package com.felix.music

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.music
 * @ClassName: PermissionUtils
 * @Author: 80341341
 * @CreateDate: 2021/8/4 16:18
 * @Description: PermissionUtils 类作用描述
 */
object PermissionUtils {
    const val REQ_PERMISSON_CODE = 101
    fun request(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 30) {
            // 先判断有没有权限
            if (!Environment.isExternalStorageManager()) {

                //跳转到设置界面引导用户打开
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + activity.packageName)
                activity.startActivityForResult(intent, 3)
            }
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).filter {
                ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            }.takeIf {
                it.isNotEmpty()
            }?.toTypedArray()?.let {
                ActivityCompat.requestPermissions(activity, it, REQ_PERMISSON_CODE)
            }
        }
    }
}