package com.felix.id3tool

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.felix.arch.mvvm.BaseViewModel
import com.felix.arch.mvvm.ListLiveData
import com.felix.arch.mvvm.ResultBean
import com.felix.resp.*
import com.felix.utils.gson.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.id3tool
 * @ClassName: AlbumViewModel
 * @Author: 80341341
 * @CreateDate: 2021/8/2 14:27
 * @Description: AlbumViewModel 类作用描述
 */
class AlbumViewModel : BaseViewModel() {
    val list: ListLiveData<Mp3Bean> = ListLiveData()
    val id3Tag = MutableLiveData<IMp3Tag.ID3Tag>()
    fun loadId3Tag(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            id3Tag.postValue(Mp3TagProxy.getID3V24(file).apply {
                if (title.isNullOrBlank()) {
                    title = file.name.let {
                        val endIndex = it.lastIndexOf('.').takeIf { it >= 0 } ?: it.length
                        it.substring(0, endIndex)
                    }
                }
            })
        }
    }

    fun search(songName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ResProxy.searchMp3(songName, 0)?.sortedWith(Comparator { o1, o2 ->
                    if (!o1.title.equals(o2.title)) {
                        if (o1.title.equals(id3Tag.value?.title)) {
                            return@Comparator -1
                        } else if (o2.title.equals(id3Tag.value?.title)) {
                            return@Comparator 1
                        } else {
                            return@Comparator o1.title?.compareTo(o2?.title ?: "")
                        }
                    }

                    if (!o1.artist.equals(o2.artist)) {
                        if (o1.artist.equals(id3Tag.value?.artist)) {
                            return@Comparator -1
                        } else if (o2.artist.equals(id3Tag.value?.artist)) {
                            return@Comparator 1
                        } else {
                            return@Comparator o1.artist?.compareTo(o2?.artist ?: "")
                        }
                    }

                    if (!(o1.album.equals(o2.album))) {
                        if (o1.album.equals(id3Tag.value?.title)) {
                            return@Comparator -1
                        } else if (o2.album.equals(id3Tag.value?.title)) {
                            return@Comparator 1
                        } else {
                            return@Comparator o1.album.compareTo(o2.album)
                        }
                    }
                    0
                }).let {
                    if (it.isEmpty()) {
                        result.postValue(ResultBean(false, 100, "查无数据"))
                    }
                    list.value?.clear()
                    list.addValue(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                result.postValue(ResultBean(false, msg = "网络异常"))
            }

        }
    }

    data class CheckHolder(
        var title: Boolean,
        var artist: Boolean,
        var album: Boolean,
        var image: Boolean
    )

    fun fillId3Tag(file: File, resource: ByteArray?, data: Mp3Bean, checkHolder: CheckHolder) {
        viewModelScope.launch(Dispatchers.IO) {
            Mp3TagProxy.getID3V24(file).run {
                var change = false
                if (checkHolder.title) {
                    title = data.title
                    change = true
                }
                if (checkHolder.artist) {
                    artist = data.artist
                    change = true
                }
                if (checkHolder.album) {
                    album = data.album
                    change = true
                }
                if (checkHolder.image) {
                    resource?.let {
                        albumImage = it
                        mimeType = "image/jpeg"
                        change = true
                    }
                }
                if (change) this else null
            }?.let {
                Log.i(TAG, "fillId3Tag: ${it.toJson()}")
                if (Mp3TagProxy.fillID3V24(file, it)) {
                    id3Tag.postValue(it)
                } else {
                    result.postValue(ResultBean(false, msg = "文件写入失败"))
                }
            }
        }
    }
}