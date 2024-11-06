package com.hoshi.armor.utils

/**
 * Created by lv.qx on 2023/11/21
 */
object FileCompatUtils {

    // 公共媒体文件的根目录
    private const val MEDIA_STORE_ROOT = "hoshi"

    /**
     * @return String? 取得 DCIM/hoshi/ 路径
     */
    fun getDCIMMinePath() = "${FilePathUtils.getDCIMPath()}/$MEDIA_STORE_ROOT/"

}