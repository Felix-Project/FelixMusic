package com.felix.id3tool

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.felix.id3tool.databinding.FileItemBinding
import com.felix.id3tool.databinding.Mp3ItemBinding
import com.felix.music.core.BaseBindingAdp
import com.felix.resp.IMp3Tag
import java.io.File

/**
 * author: felix
 * created on: 2021/8/1 22:55
 * description: FileAdp 的描述
 */
class FileAdp : BaseBindAdp2<FileHolder, FileItemBinding, Mp3ItemBinding>() {

    override fun getBindging(parent: ViewGroup, viewType: Int) = if (viewType == VIEW_TYPE1) {
        FileItemBinding.inflate(parent.layoutInflater, parent, false)
    } else {
        Mp3ItemBinding.inflate(parent.layoutInflater, parent, false)
    }


    override fun onDataChange(binding: FileItemBinding, data: FileHolder, pos: Int, size: Int) {
        binding.apply {
            tvName.text = data.file.name
            tvName.isSelected = data.file.isDirectory
        }
    }

    override fun onDataChange2(binding: Mp3ItemBinding, data: FileHolder, pos: Int, size: Int) {
        binding.apply {
            tvTitle.text = data.iD3Tag?.title ?: data.file.name
            tvArtist.text = data.iD3Tag?.artist ?: ""
            tvAlbum.text = data.iD3Tag?.album ?: ""
            data.iD3Tag?.albumImage?.let {
                Glide.with(ivAlbum).load(it).into(ivAlbum)
            } ?: kotlin.run {
                Glide.with(ivAlbum).load(R.drawable.ic_def_album).into(ivAlbum)
            }
        }
    }
}

data class FileHolder(var file: File, var iD3Tag: IMp3Tag.ID3Tag?) : Selectable {
    override fun select(): Boolean {
        return file.isDirectory
    }
}