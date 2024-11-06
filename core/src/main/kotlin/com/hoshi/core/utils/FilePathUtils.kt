package com.hoshi.armor.utils

import android.os.Environment

/**
 * Created by lv.qx on 2024/5/22
 * 抽时间放到 core 库里面
 */
object FilePathUtils {

    fun getDCIMPath(): String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath

}