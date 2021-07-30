package com.felix.resp.utils

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.resp.utils
 * @ClassName: ByteUtils
 * @Author: 80341341
 * @CreateDate: 2021/7/29 10:17
 * @Description: ByteUtils类作用描述
 */
fun ByteArray.toInt(): Int {
    return this[3].toInt() and 0xFF or (
            this[2].toInt() and 0xFF shl 8) or (
            this[1].toInt() and 0xFF shl 16) or (
            this[0].toInt() and 0xFF shl 24)
}

fun Int.toByteArray(): ByteArray {
    return byteArrayOf(
        (this shr 24 and 0xFF).toByte(),
        (this shr 16 and 0xFF).toByte(),
        (this shr 8 and 0xFF).toByte(),
        (this and 0xFF).toByte()
    )
}