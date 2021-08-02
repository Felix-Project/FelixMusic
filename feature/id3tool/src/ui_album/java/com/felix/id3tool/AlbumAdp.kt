package com.felix.id3tool

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.felix.id3tool.databinding.AlbumItemBinding
import com.felix.music.core.BaseBindingAdp
import com.felix.resp.IMp3Tag
import com.felix.resp.Mp3TagProxy
import com.felix.resp.SongBean
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
class AlbumAdp : BaseBindingAdp<SongBean, AlbumItemBinding>() {
    lateinit var file: File
    override fun getBindging(parent: ViewGroup, viewType: Int): AlbumItemBinding {
        return AlbumItemBinding.inflate(parent.layoutInflater, parent, false)
    }

    var albumCallback: ((ByteArray?, SongBean) -> Unit)? = null
    override fun onDataChange(binding: AlbumItemBinding, data: SongBean, pos: Int, size: Int) {
        binding.apply {
            tvTitle.text = data.title
            tvArtist.text = data.artist
            tvAlbum.text = data.album?.title ?: ""
            data.album?.thumb?.let {
                Glide.with(ivAlbum)
                    .load(it.photo_135)
                    .error(R.drawable.ic_album_error)
                    .into(ivAlbum)
            } ?: kotlin.run {
                ivAlbum.setImageResource(R.drawable.ic_def_album)
            }

            fdvDonwload.reset()

            fdvDonwload.setOnClickListener {
                data.album?.thumb?.photo_1200?.let {
                    Glide.with(ivAlbum)
                        .`as`(ByteArray::class.java)
                        .load(data.album?.thumb?.photo_1200)
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