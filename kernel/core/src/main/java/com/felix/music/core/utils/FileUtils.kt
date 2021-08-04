package com.felix.music.core.utils

import android.os.Environment
import java.io.File

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.music.core.utils
 * @ClassName: FileUtils
 * @Author: 80341341
 * @CreateDate: 2021/8/4 18:01
 * @Description: FileUtils 类作用描述
 */

fun File.isSdcard(): Boolean {
    return this.absolutePath.equals(Environment.getExternalStorageDirectory().absolutePath)
}