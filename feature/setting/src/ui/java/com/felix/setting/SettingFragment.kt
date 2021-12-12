package com.felix.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.felix.lib_arch.mvvm.BaseFragment
import com.felix.setting.databinding.SettingFragmentBinding

/**
 * author: felix
 * created on: 2021/12/12 20:02
 * description: SettingFragment 的描述
 */

class SettingFragment : BaseFragment() {
    lateinit var binding: SettingFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      return  SettingFragmentBinding.inflate(inflater, container, false).run {
            binding = this
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {

        }
    }

}