package com.felix.resp

import android.content.Context
import android.os.Environment
import com.felix.resp.utils.writeID3V2
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.RandomAccessFile
import java.nio.charset.Charset

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.resp
 * @ClassName: TestID3
 * @Author: 80341341
 * @CreateDate: 2021/7/27 16:25
 * @Description: TestID3类作用描述
 */
class TestID3 {
    companion object {

        fun test(context: Context) {

        }

        val root = File("feature/resp/res")

        val origin = File(root, "origin.mp3")

        @JvmStatic
        fun main(args: Array<String>) {

            val str: String = "有何不可"
            arrayOf(
                Charsets.UTF_16,
                Charsets.UTF_16LE,
                Charsets.UTF_8,
                Charsets.ISO_8859_1,
                Charsets.US_ASCII,
                Charsets.UTF_16BE,
                Charsets.UTF_32,
                Charsets.UTF_32BE,
                Charsets.UTF_32LE,
                Charset.forName("GBK")
            ).map {
                val toByteArray = str.toByteArray(it).map {
                    Integer.toHexString(it.toUByte().toInt())
                }.forEach {
                    print("$it   ")
                }
                println("charsets=$it")
            }

            return
            val byteArray = byteArrayOf(
                0x01,
                0xFF.toByte(),
                0xFE.toByte(),
                0x09,
                0x67,
                0x55,
                0x4F,
                0x0D,
                0x4E,
                0xEF.toByte(),
                0x53
            ).let { bytes ->
                arrayOf(
                    Charsets.UTF_16,
                    Charsets.UTF_16LE,
                    Charsets.UTF_8,
                    Charsets.ISO_8859_1,
                    Charsets.US_ASCII,
                    Charsets.UTF_16BE,
                    Charsets.UTF_32,
                    Charsets.UTF_32BE,
                    Charsets.UTF_32LE,
                    Charset.forName("GBK")
                ).map {
                    val str = String(bytes, it)
                    println("str=$str,charsets=$it")

                }
            }


            return
            File(root, "test.mp3").also {
                if (it.exists()) {
                    it.delete()
                }
                it.createNewFile()
            }.also {
                it.outputStream().sink().buffer()
                    .writeAll(origin.inputStream().source())
            }.let {
                it.writeID3V2("有何不可", "许嵩", "有何不可专辑")
            }
        }

        @JvmStatic
        fun main(context: Context) {
            val rootAndrolid = File(Environment.getExternalStorageDirectory(), "aaaDownload")

            if (rootAndrolid.exists().not()) {
                rootAndrolid.mkdirs()
            }
            File(rootAndrolid, "test.mp3").also {
                if (it.exists()) {
                    it.delete()
                }
                it.createNewFile()
            }.also {
                it.outputStream().sink().buffer()
                    .writeAll(context.assets.open("origin.mp3").source())
            }.let {
                it.writeID3V2("有何不可", "许嵩", "有何不可专辑")
            }
        }
    }
}