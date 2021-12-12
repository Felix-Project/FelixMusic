package com.felix.setting

import androidx.fragment.app.Fragment

/**
 * author: felix
 * created on: 2021/12/12 20:01
 * description: SettingImpl 的描述
 */
class SettingImpl : ISetting {
    override fun newFragment(): Fragment {
        return SettingFragment()
    }
}