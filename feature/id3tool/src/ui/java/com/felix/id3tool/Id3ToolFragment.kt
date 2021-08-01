package com.felix.id3tool

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.felix.id3tool.databinding.FragmentId3ToolBinding
import java.io.File

/**
 * author: felix
 * created on: 2021/8/1 18:49
 * description: Id3ToolFragment 的描述
 */
class Id3ToolFragment : Fragment(), FileExploreCallback {
    val fileExploreFragment = FileExploreFragment().apply {
        fileExploreCallback = this@Id3ToolFragment
    }
    val aLbumFragment = ALbumFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentId3ToolBinding.inflate(layoutInflater, container, false).apply {
            childFragmentManager.beginTransaction().also {
                it.add(
                    R.id.id3Container,
                    fileExploreFragment,
                    FileExploreFragment::class.java.name
                )
            }.commitAllowingStateLoss()
        }.root
    }

    override fun goMp3File(file: File) {
        aLbumFragment.arguments = (aLbumFragment.arguments ?: Bundle()).apply {
            putSerializable(ALbumFragment.KEY_PARM_FILE, file)
        }
        childFragmentManager.findFragmentByTag(aLbumFragment::class.java.name)?.let { fragment ->
            childFragmentManager.beginTransaction().also {
                it.show(fragment)
                it.hide(fileExploreFragment)
            }.commitAllowingStateLoss()
        } ?: kotlin.run {
            childFragmentManager.beginTransaction().also {
                it.add(
                    R.id.id3Container,
                    aLbumFragment,
                    aLbumFragment::class.java.name
                )
                it.hide(fileExploreFragment)
            }.commitAllowingStateLoss()
        }
    }

}