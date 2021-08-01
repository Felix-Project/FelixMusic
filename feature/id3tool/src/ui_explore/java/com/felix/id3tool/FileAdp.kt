package com.felix.id3tool

import android.view.ViewGroup
import com.felix.id3tool.databinding.FileItemBinding
import com.felix.music.core.BaseBindingAdp
import java.io.File

/**
 * author: felix
 * created on: 2021/8/1 22:55
 * description: FileAdp 的描述
 */
class FileAdp : BaseBindingAdp<File, FileItemBinding>() {
    override fun getBindging(parent: ViewGroup, viewType: Int): FileItemBinding {
        return FileItemBinding.inflate(parent.layoutInflater, parent, false)
    }

    override fun onDataChange(binding: FileItemBinding, data: File, pos: Int, size: Int) {
        binding.apply {
            tvName.text = data.name
            tvName.isSelected = data.isDirectory
        }
    }
}