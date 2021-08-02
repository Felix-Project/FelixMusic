package com.felix.id3tool

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.felix.arch.mvvm.BaseViewModel
import com.felix.arch.mvvm.ListLiveData
import com.felix.resp.Mp3TagProxy
import com.felix.utils.ext.doOnItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
    val fileList = ListLiveData<FileHolder>()
    private var job: Job? = null
    fun load(rootFile: File? = null) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val result = (rootFile?.also {
                parent.postValue(it)
            } ?: parent.value)?.listFiles()?.filter {
                !it.name.startsWith(".") &&
                        (it.isDirectory || it.name.endsWith(".mp3"))
            }?.sortedBy {
                return@sortedBy if (it.name == "felix")
                    "11111"
                else if (it.isDirectory) {
                    "1111".plus(it.name)
                } else {
                    it.name
                }
            }?.map {
                FileHolder(it, null)
            }?.also {
                fileList.value?.clear()
                fileList.addValue(it)
            }?.doOnItem { fileHolder ->
                if (!fileHolder.file.isDirectory) {
                    Mp3TagProxy.getID3V24(fileHolder.file).let {
                        if (it.title.isNullOrBlank()) {
                            it.title = fileHolder.file.name
                        }
                        fileHolder.iD3Tag = it
                    }
                }
            } ?: emptyList()
            fileList.value?.clear()
            fileList.addValue(result)
        }
    }
}