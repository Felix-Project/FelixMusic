package com.felix.id3tool

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.felix.id3tool.databinding.AlbumItemBinding
import com.felix.music.core.BaseBindingAdp
import com.felix.resp.Mp3Bean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.id3tool
 * @ClassName: AlbumAdp
 * @Author: 80341341
 * @CreateDate: 2021/8/2 14:37
 * @Description: AlbumAdp 类作用描述
 */
class AlbumAdp : BaseBindingAdp<Mp3Bean, AlbumItemBinding>() {
    lateinit var file: File
    override fun getBindging(parent: ViewGroup, viewType: Int): AlbumItemBinding {
        return AlbumItemBinding.inflate(parent.layoutInflater, parent, false)
    }

    var albumCallback: ((ByteArray?, Mp3Bean) -> Unit)? = null
    override fun onDataChange(binding: AlbumItemBinding, data: Mp3Bean, pos: Int, size: Int) {
        binding.apply {
            tvTitle.text = data.title
            tvArtist.text = data.artist
            tvAlbum.text = data.album
            data.albumImageThumb?.let {
                Glide.with(ivAlbum)
                    .load(it)
                    .error(R.drawable.ic_album_error)
                    .into(ivAlbum)
            } ?: kotlin.run {
                ivAlbum.setImageResource(R.drawable.ic_def_album)
            }

            fdvDonwload.reset()

            fdvDonwload.setOnClickListener {
                data.albumImageOrigin?.let {
                    Glide.with(ivAlbum)
                        .`as`(ByteArray::class.java)
                        .load(it)
                        .addListener(object : RequestListener<ByteArray> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<ByteArray>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                fdvDonwload.showDownloadError()
                                return true
                            }

                            override fun onResourceReady(
                                resource: ByteArray?,
                                model: Any?,
                                target: Target<ByteArray>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                scope.launch(Dispatchers.IO) {
                                    albumCallback?.invoke(resource, data)
                                    launch(Dispatchers.Main) {
                                        fdvDonwload.showDownloadOk()
                                    }

                                }
                                return true
                            }
                        }).submit()
                } ?: kotlin.run {
                    albumCallback?.invoke(null, data)
                    fdvDonwload.showDownloadOk()
                }
                fdvDonwload.startDownload()
            }
        }
    }

}