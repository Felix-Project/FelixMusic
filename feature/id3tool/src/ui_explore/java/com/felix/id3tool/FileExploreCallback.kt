package com.felix.id3tool

import java.io.File

/**
 * author: felix
 * created on: 2021/8/2 0:31
 * description: FileExploreCallback 的描述
 */
interface FileExploreCallback {
    fun goMp3File(file: File)
}