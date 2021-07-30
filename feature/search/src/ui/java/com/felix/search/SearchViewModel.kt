package com.felix.search

import android.os.Environment
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.felix.arch.mvvm.BaseViewModel
import com.felix.arch.mvvm.ListLiveData
import com.felix.download.DownloadProxy
import com.felix.download.IDownload
import com.felix.lib_app_tools.toast.ToastDelegate
import com.felix.resp.ResProxy
import com.felix.resp.SongBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class SearchViewModel : BaseViewModel() {
    val list: ListLiveData<SongBean> = ListLiveData()
    private var currentName: String = ""
    private var currentPage = 0

    fun search(songName: String, page: Int = 0) {
        currentName = songName
        currentPage = page
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ResProxy.searchMp3(songName, page)?.let {
                    if (it.isEmpty()) {
                        viewModelScope.launch {
                            ToastDelegate.show("查无数据")
                        }
                    }
                    list.value?.clear()
                    list.addValue(it)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                viewModelScope.launch {
                    ToastDelegate.show("网络异常")
                }
            }

        }
    }

    fun getNext() {
        search(currentName, ++currentPage)
    }
}