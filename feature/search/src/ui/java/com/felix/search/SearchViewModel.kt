package com.felix.search

import androidx.lifecycle.viewModelScope
import com.felix.arch.mvvm.BaseViewModel
import com.felix.arch.mvvm.ListLiveData
import com.felix.arch.mvvm.ResultBean
import com.felix.lib_app_tools.toast.ToastDelegate
import com.felix.resp.ResProxy
import com.felix.resp.SongBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun getNext() {
        search(currentName, ++currentPage)
    }
}