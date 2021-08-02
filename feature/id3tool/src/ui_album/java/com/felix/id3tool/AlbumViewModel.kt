package com.felix.id3tool

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.felix.arch.mvvm.BaseViewModel
import com.felix.arch.mvvm.ListLiveData
import com.felix.arch.mvvm.ResultBean
import com.felix.resp.IMp3Tag
import com.felix.resp.Mp3TagProxy
import com.felix.resp.ResProxy
import com.felix.resp.SongBean
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
    val list: ListLiveData<SongBean> = ListLiveData()
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
                var searchMp3 = ResProxy.searchMp3(songName, 0)
                if (searchMp3.isNullOrEmpty()) {
                    searchMp3 = ResProxy.searchMp3(songName, 0)
                }
                searchMp3?.let {
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

    fun fillId3Tag(file: File, resource: ByteArray?, data: SongBean, checkHolder: CheckHolder) {
        viewModelScope.launch(Dispatchers.IO) {
            Mp3TagProxy.getID3V24(file).run {
                var change = false
                if (checkHolder.title) {
                    title = data.title ?: ""
                    change = true
                }
                if (checkHolder.artist) {
                    artist = data.artist ?: ""
                    change = true
                }
                if (checkHolder.album) {
                    album = data.album?.title ?: ""
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
                Mp3TagProxy.fillID3V24(file, it)
                id3Tag.postValue(it)
            }
        }
    }
}