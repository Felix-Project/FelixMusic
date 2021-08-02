package com.felix.id3tool

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.felix.arch.mvvm.BaseMvvmFragment
import com.felix.id3tool.databinding.FragmentAlbumBinding
import java.io.File
import java.lang.NullPointerException

/**
 * author: felix
 * created on: 2021/8/1 18:49
 * description: ALbumFragment的描述
 */
class AlbumFragment : BaseMvvmFragment<AlbumViewModel>() {
    companion object {
        const val KEY_PARM_FILE = "PARM_FILE"
    }

    var albumAdp: AlbumAdp = AlbumAdp()
    lateinit var binding: FragmentAlbumBinding
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
            albumAdp.file = rootFile
        } ?: kotlin.run {
            throw NullPointerException("file must not be null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAlbumBinding.inflate(inflater, container, false).apply {
        binding = this
        etKeyword.hint = rootFile.name
        rvMusicList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMusicList.adapter = albumAdp
        viewModel.search(rootFile)
        observe(viewModel.list) {
            dismissLoading()
            albumAdp.datas = it
        }
        observe(viewModel.result) {
            dismissLoading()
        }
        etKeyword.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                etKeyword.text?.toString()?.takeIf { it.isNotBlank() }?.let {
                    showLoading("正在搜索${etKeyword.text.toString()}")
                    viewModel.search(it)
                } ?: kotlin.run {
                    showLoading("正在搜索${etKeyword.hint}")
                    viewModel.search(rootFile)
                }

            }
            true
        }
    }.root

    private fun hideKeyboard() {
        context?.getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
            it as InputMethodManager
        }?.hideSoftInputFromWindow(binding.etKeyword.windowToken, 0)
    }
}