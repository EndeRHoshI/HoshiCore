package com.hoshi.core.extentions

import java.io.File

/**
 * 判断一个文件是否是音乐文件
 */
val File.isMusic
    get() = listOf(".wav", ".mp3", ".aac", ".ogg", ".wma", ".m4a").contains(suffix)

/**
 * 判断一个文件是否是视频文件
 */
val File.isVideo
    get() = listOf(".mp4", ".rmvb", ".avi").contains(suffix)

/**
 * 取得后缀名，如 .mp4、.mp3、.m4a
 */
val File.suffix
    get() = if (!name.contains(".")) {
        // 如果不含有 . ，无法读取后缀，直接返回空
        ""
    } else {
        name.substring(name.lastIndexOf("."), name.length)
    }

/**
 * 取得无后缀文件名
 *
 * @receiver File
 * @return String
 */
fun File.getPureName(): String {
    val fileName = name
    return fileName.substring(0, fileName.lastIndexOf('.'))
}