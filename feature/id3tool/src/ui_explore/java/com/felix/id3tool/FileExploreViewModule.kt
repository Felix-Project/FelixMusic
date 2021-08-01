package com.felix.id3tool

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.felix.arch.mvvm.BaseViewModel
import com.felix.arch.mvvm.ListLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * author: felix
 * created on: 2021/8/1 22:43
 * description: FileExploreViewModule 的描述
 */
class FileExploreViewModule : BaseViewModel() {
    val root = Environment.getExternalStorageDirectory()
    val parent = MutableLiveData(root)
    val fileList = ListLiveData<File>()
    fun load(file: File? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = (file?.also {
                parent.postValue(it)
            } ?: parent.value)?.listFiles()?.filter {
                !it.name.startsWith(".") &&
                        (it.isDirectory || it.name.endsWith(".mp3"))
            }?.sortedBy {
                return@sortedBy if (it.name == "felix")
                    "11111"
                else
                    it.name
            } ?: emptyList<File>()
            fileList.value?.clear()
            fileList.addValue(result)
        }
    }
}