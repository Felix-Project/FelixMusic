package com.felix.id3tool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felix.arch.mvvm.BaseMvvmFragment
import com.felix.id3tool.databinding.FragmentFileExploreBinding
import com.felix.lib_app_tools.toast.ToastDelegate
import java.io.File

/**
 * author: felix
 * created on: 2021/8/1 18:49
 * description: ExploreFragment的描述
 */
class FileExploreFragment : BaseMvvmFragment<FileExploreViewModule>() {
    lateinit var fileAdp: FileAdp
    lateinit var fileExploreCallback: FileExploreCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFileExploreBinding.inflate(inflater, container, false).apply {
        fileAdp = FileAdp()
        fileAdp.onItemClickListener = { view: View, data: File, position: Int, size: Int ->
            if (data.isDirectory) {
                viewModel.load(data)
            } else {
                fileExploreCallback.goMp3File(data)
                ToastDelegate.show("这是mp3文件")
            }
        }
        rvFileList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFileList.adapter = fileAdp
        viewModel.load()
        observe(viewModel.fileList) {
            fileAdp.datas = it
        }
        observe(viewModel.parent) {
            val isSdCard = it.absolutePath == viewModel.root.absolutePath
            tvBack.isEnabled = !isSdCard
            tvBack.tag = it.takeIf { !isSdCard }?.let { it.parentFile }
            tvBack.text = it.takeIf { isSdCard }?.let {
                it.name
            } ?: kotlin.run {
                it.parentFile.name
            }
        }
        tvBack.setOnClickListener {
            it?.tag?.takeIf { it is File }?.let {
                it as File
            }?.let {
                viewModel.load(it)
            }
        }
    }.root
}