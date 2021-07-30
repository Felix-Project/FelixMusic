package com.felix.search

import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.felix.download.DownloadProxy
import com.felix.download.IDownload
import com.felix.lib_app_tools.toast.ToastDelegate
import com.felix.music.core.BaseBindingAdp
import com.felix.resp.IMp3Tag
import com.felix.resp.Mp3TagProxy
import com.felix.resp.SongBean
import com.felix.search.databinding.MusicItmBinding
import com.felix.utils.handler.UIDelegate
import java.io.File

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.search
 * @ClassName: SearchAdp
 * @Author: 80341341
 * @CreateDate: 2021/7/26 19:04
 * @Description: SearchAdp类作用描述
 */
class SearchAdp : BaseBindingAdp<SongBean, MusicItmBinding>() {
    override fun getBindging(parent: ViewGroup, viewType: Int): MusicItmBinding {
        return MusicItmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onDataChange(binding: MusicItmBinding, data: SongBean, pos: Int, size: Int) {
        binding.apply {
            tvTitle.text = data.title
            tvArtist.text = data.artist
            tvAlbum.text = data.album?.title ?: ""
            data.album?.thumb?.let {
                var url = it.photo_135
                if (url == null) {
                    url = it.photo_68
                }
                if (url == null) {
                    url = it.photo_270
                }
                if (url == null) {
                    url = it.photo_34
                }
                if (url == null) {
                    url = it.photo_300
                }
                if (url == null) {
                    url = it.photo_600
                }
                if (url == null) {
                    url = it.photo_1200
                }
                Glide.with(ivAlbum)
                    .load(url)
                    .placeholder(R.drawable.ic_def_album)
                    .error(R.drawable.ic_album_error)
                    .into(ivAlbum)
            }
            btnDownload.setOnClickListener {
                download(binding, data)
            }
            fdvDonwload.reset()
            fdvDonwload.setOnClickListener {
                download(binding, data)
                fdvDonwload.startDownload()
            }

        }
    }

    private fun download(binding: MusicItmBinding, songBean: SongBean) {
        IDownload.DownloadConfig(
            url = songBean.url ?: "",
            name = "${songBean.title} - ${songBean.artist}",
            File(Environment.getExternalStorageDirectory(), "felix/Music/Like")
        ).let { config ->
            DownloadProxy.download(config, { file ->
                Log.d(TAG, "onCompelete: download success.")
                IMp3Tag.ID3Tag(
                    title = songBean.title ?: "",
                    artist = songBean.artist ?: "",
                    album = songBean.album?.title ?: ""
                ).let {
                    Mp3TagProxy.writeID3V24(file, it)
                }
                Log.d(TAG, "onCompelete: writeID3V2 success.")
                UIDelegate.post {
                    binding.fdvDonwload.showDownloadOk()
                    ToastDelegate.show(file.absolutePath)
                }
            }, {
                Log.e(TAG, "onError: ", it)
                UIDelegate.post {
                    binding.fdvDonwload.showDownloadError()
                }
            }, { downloaded, total ->
                Log.i(TAG, "onProgress: downloaded=$downloaded,total=$total")
                UIDelegate.post {
                    binding.fdvDonwload.upDateProgress(downloaded * 1f / total)
                }
            })
        }
    }
}