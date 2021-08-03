package com.felix.resp

/**
 *
 * @ProjectName: FelixMusic
 * @Package: com.felix.resp
 * @ClassName: Mp3Bean
 * @Author: 80341341
 * @CreateDate: 2021/8/3 20:00
 * @Description: Mp3Bean 类作用描述
 */
data class Mp3Bean(
    var title: String = "",
    var artist: String = "",
    var album: String = "",
    var url: String = "",
    var albumImageThumb: String? = null,
    var albumImage: String? = null,
    var albumImageOrigin: String? = null
)