package com.felix.setting

import android.app.Application
import com.felix.music.core.IAppInit

/**
 * author: felix
 * created on: 2021/12/12 19:59
 * description: SettingAppInit 的描述
 */
class SettingAppInit : IAppInit {
    override fun onCreate(app: Application, flavor: String, vararg args: String) {
        SettingProxy.iSetting = SettingImpl()
    }
}