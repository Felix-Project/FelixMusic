package com.felix.id3tool

import android.content.Context
import android.graphics.BitmapFactory
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
import com.bumptech.glide.Glide
import com.felix.arch.mvvm.BaseMvvmFragment
import com.felix.id3tool.databinding.FragmentAlbumBinding
import com.felix.resp.Mp3TagProxy
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
            viewModel.loadId3Tag(rootFile)
        } ?: kotlin.run {
            throw NullPointerException("file must not be null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAlbumBinding.inflate(inflater, container, false).apply {
        binding = this
        rvMusicList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMusicList.adapter = albumAdp.apply {
            albumCallback = { resource, data ->
                AlbumViewModel.CheckHolder(
                    title = chkTitle.isChecked,
                    artist = chkArtist.isChecked,
                    album = chkAlbum.isChecked,
                    image = chkImage.isChecked
                ).let {
                    viewModel.fillId3Tag(file, resource, data, it)
                }
            }
        }
        ivRefresh.setOnClickListener {
            viewModel.id3Tag?.value?.let {
                viewModel.search(it.title)
            } ?: kotlin.run {
                viewModel.loadId3Tag(rootFile)
            }
        }

        observe(viewModel.list) {
            dismissLoading()
            albumAdp.datas = it
        }
        observe(viewModel.result) {
            dismissLoading()
        }
        observe(viewModel.id3Tag) {
            if (albumAdp.datas.isNullOrEmpty()) {
                showLoading("正在搜索${it.title}")
                viewModel.search(it.title)
            }
            tvOriginTitle.text = it.title
            tvOriginArtist.text = it.artist
            tvOriginAlbum.text = it.album
            it.albumImage?.let {
                Glide.with(ivOriginAlbum).load(it).into(ivOriginAlbum)
            }
            chkTitle.isChecked = it.title.isBlank()
            chkArtist.isChecked = it.title.isBlank()
            chkAlbum.isChecked = it.title.isBlank()
            chkImage.isChecked = it.albumImage?.isEmpty() ?: true

        }
        viewModel.loadId3Tag(rootFile)


//        etKeyword.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                etKeyword.text?.toString()?.takeIf { it.isNotBlank() }?.let {
//                    showLoading("正在搜索${etKeyword.text.toString()}")
//                    viewModel.search(it)
//                } ?: kotlin.run {
//                    showLoading("正在搜索${etKeyword.hint}")
//                    viewModel.search(rootFile)
//                }
//
//            }
//            true
//        }
    }.root

    private fun hideKeyboard() {
//        context?.getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
//            it as InputMethodManager
//        }?.hideSoftInputFromWindow(binding.etKeyword.windowToken, 0)
    }
}