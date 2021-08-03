package com.felix.id3tool

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.felix.arch.mvvm.BaseMvvmFragment
import com.felix.id3tool.databinding.FragmentAlbumBinding
import com.felix.lib_app_tools.toast.ToastDelegate
import java.io.File

/**
 * author: felix
 * created on: 2021/8/1 18:49
 * description: ALbumFragment的描述
 */
class AlbumFragment : BaseMvvmFragment<AlbumViewModel>() {
    companion object {
        const val KEY_PARM_FILE = "PARM_FILE"
    }

    var albumCallback: AlbumCallback? = null

    var albumAdp: AlbumAdp = AlbumAdp()
    lateinit var binding: FragmentAlbumBinding
    private lateinit var rootFile: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFile()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            initFile()
        }
    }

    private fun initFile() {
        arguments?.getSerializable(KEY_PARM_FILE)?.takeIf { it is File }?.let {
            it as File
        }?.let {
            rootFile = it
            albumAdp.file = rootFile
            text = ""
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
        tvFileNmae.text = rootFile.name.let { it.substring(0, it.lastIndexOf(".")) }
        chkImage.setOnCheckedChangeListener { buttonView, isChecked ->
            albumAdp.downloadImage = isChecked
        }
        ivRefresh.setOnClickListener {
            viewModel.id3Tag?.value?.let {
                viewModel.search(it.title)
            } ?: kotlin.run {
                viewModel.loadId3Tag(rootFile)
            }
        }
        ivRefresh.setOnLongClickListener {
            showdialog()
            true
        }

        ivBack.setOnClickListener {
            albumCallback?.backToExplore()
        }

        observe(viewModel.list) {
            dismissLoading()
            albumAdp.datas = it
        }
        observe(viewModel.result) {
            ToastDelegate.show(it.msg)
            dismissLoading()
        }
        observe(viewModel.id3Tag) {
            if (text.isNullOrBlank()) {
                text = it.title
            }
            tvFileNmae.text = rootFile.name.let { it.substring(0, it.lastIndexOf(".")) }
            showLoading("正在搜索${it.title}")
            viewModel.search(it.title)
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

    private var text = ""
    fun showdialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Title")

        // Set up the input
        val input = EditText(context)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter Text")
        input.setText(text)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK", { dialog, which ->
            // Here you get get input text from the Edittext
            text = input.text.toString()
            viewModel.search(text)
        })
        builder.setNegativeButton(
            "Cancel",
            { dialog, which -> dialog.cancel() })

        builder.show()
    }
}