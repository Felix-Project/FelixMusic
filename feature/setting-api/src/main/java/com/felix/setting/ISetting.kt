package com.felix.setting

import androidx.fragment.app.Fragment

/**
 * author: felix
 * created on: 2021/12/12 19:58
 * description: ISetting 的描述
 */
interface ISetting {
    fun newFragment(): Fragment
}

object SettingProxy : ISetting {
    var iSetting: ISetting? = null
    override fun newFragment(): Fragment {
        return iSetting?.newFragment() ?: Fragment()
    }
}