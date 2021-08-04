package com.felix.id3tool

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felix.arch.mvvm.BaseMvvmFragment
import com.felix.id3tool.databinding.FragmentFileExploreBinding
import com.felix.lib_app_tools.toast.ToastDelegate
import com.felix.music.core.utils.isSdcard
import java.io.File

/**
 * author: felix
 * created on: 2021/8/1 18:49
 * description: ExploreFragment的描述
 */
class FileExploreFragment : BaseMvvmFragment<FileExploreViewModule>() {
    lateinit var fileAdp: FileAdp
    lateinit var fileExploreCallback: FileExploreCallback
    lateinit var binding: FragmentFileExploreBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFileExploreBinding.inflate(inflater, container, false).apply {
        binding = this
        fileAdp = FileAdp()
        fileAdp.onItemClickListener = { view: View, data: FileHolder, position: Int, size: Int ->
            if (data.file.isDirectory) {
                viewModel.load(data.file)
            } else {
                fileExploreCallback.goMp3File(data.file)
            }
        }
        fileAdp.onLongItemClickListener =
            { view: View, data: FileHolder, position: Int, size: Int ->
                if (!data.select()) {
                    AlertDialog.Builder(context)
                        .setPositiveButton("确定", { dialog, which ->
                            data.file.delete()
                            viewModel.load()
                        })
                        .setNegativeButton("取消", { dialog, which ->
                            dialog.dismiss()
                        })
                        .setMessage("是否确认删除${data.file.name}")
                        .create().show()
                }
                true
            }

        ivHome.setOnClickListener {
            viewModel.load(viewModel.root)
        }
        rvFileList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFileList.adapter = fileAdp
        viewModel.load()
        observe(viewModel.fileList) {
            fileAdp.datas = it
        }
        observe(viewModel.parent) {
            val isSdCard = it.isSdcard()
            tvBack.visibility = takeIf { isSdCard }?.let { View.INVISIBLE } ?: View.VISIBLE
            tvBack.tag = it.takeIf { !isSdCard }?.let { it.parentFile }
            tvBack.text = it.takeIf { isSdCard }?.let {
                it.name
            } ?: kotlin.run {
                return@run if (it.parentFile.isSdcard()) {
                    "sdcard"
                } else {
                    it.parentFile.name
                }
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            viewModel.load()
        }
    }

    override fun onBackPress(): Boolean {
        if (super.onBackPress()) {
            return true
        }
        if (viewModel?.parent?.value?.isSdcard() ?: true) {
            return false
        }
        binding.tvBack.performClick()
        return true
    }
}