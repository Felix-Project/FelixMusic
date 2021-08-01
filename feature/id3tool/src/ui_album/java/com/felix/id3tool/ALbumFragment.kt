package com.felix.id3tool

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.felix.id3tool.databinding.FragmentAlbumBinding
import java.io.File
import java.lang.NullPointerException

/**
 * author: felix
 * created on: 2021/8/1 18:49
 * description: ALbumFragment的描述
 */
class ALbumFragment : Fragment() {
    companion object {
        const val KEY_PARM_FILE = "PARM_FILE"
    }

    private lateinit var rootFile: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFile()
    }

    override fun onResume() {
        super.onResume()
        initFile()
    }

    private fun initFile() {
        arguments?.getSerializable(KEY_PARM_FILE)?.takeIf { it is File }?.let {
            it as File
        }?.let {
            rootFile = it
        } ?: kotlin.run {
            throw NullPointerException("file must not be null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAlbumBinding.inflate(inflater, container, false).apply {
        tvFileTitle.text = rootFile.name
    }.root
}