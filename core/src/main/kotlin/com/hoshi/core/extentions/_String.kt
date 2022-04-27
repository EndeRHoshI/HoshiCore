package com.hoshi.core.extentions

import android.net.Uri
import androidx.annotation.IntRange
import androidx.core.net.toUri
import androidx.core.text.isDigitsOnly
import java.io.File
import java.util.regex.Pattern
import kotlin.random.Random

fun String.wrappedIn(targetStr: String) {
    wrappedIn(targetStr, targetStr)
}

fun String.wrappedIn(startStr: String, endStr: String): String {
    return startStr + this + endStr
}

/**
 * 判断是否是网络地址
 */
fun String?.isNetworkUrl(): Boolean {
    return if (this.isNullOrEmpty()) {
        false
    } else {
        val head = "(http://|ftp://|https://|www)"
        val pattern = Pattern.compile("$head[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr|[0-9]{1,3})[^\u4e00-\u9fa5\\s]*")
        pattern.matcher(this).matches()
    }
}

/**
 * 判断是电话号码，长度为 11 且都是数字且为 1 开头，不做正则匹配，交给后台处理
 */
fun String.isPhoneNum() = this.length == 11 && this.isDigitsOnly() && this.startsWith("1")

/**
 * 判断不是电话号码
 */
fun String.isNotPhoneNum() = !isPhoneNum()

/**
 * 移除区号
 */
fun String.removeAreaCode() = replace("+86-", "")

/**
 * 手机号脱敏
 */
fun String.desensitization() = if (isNotPhoneNum()) {
    this
} else {
    replaceRange(3, 7, "****")
}

/**
 * 判断密码是否合法，当前规则：数字或字母组成，6-16位
 */
fun String.isPwdLegal() = Pattern.compile("^[A-Za-z0-9]{6,16}$").matcher(this).matches()

fun String.isVerifyCodeLegal() = Pattern.compile("^[0-9]{6}$").matcher(this).matches()

/**
 * 当字符串为 null 或空字符串时，返回默认值
 */
fun String?.orDefault(defaultStr: String = "") = if (this.isNullOrEmpty()) {
    defaultStr
} else {
    this
}

/**
 * 本地路径加上 file:// 开头
 *
 * 形如 /storage/emulated/0/Android/data/com.hoshi.armor/files/Music/leisure_4.m4a
 *                                ||
 * file:///storage/emulated/0/Android/data/com.hoshi.armor/files/Music/leisure_4.m4a
 */
fun String?.toFileUriStr(): String? {
    return if (this != null) {
        Uri.fromFile(File(this)).toString()
    } else {
        null
    }
}

/**
 * 本地路径加上 file:// 开头
 *
 * 形如 /storage/emulated/0/Android/data/com.hoshi.armor/files/Music/leisure_4.m4a
 *                                ||
 * file:///storage/emulated/0/Android/data/com.hoshi.armor/files/Music/leisure_4.m4a
 */
fun String?.toFileUri(): Uri? {
    return if (this != null) {
        if (this.startsWith("content://")) return this.toUri() // 如果已经有 content 头，不作转换直接返回
        Uri.fromFile(File(this))
    } else {
        null
    }
}

/**
 * 生成随机字符串
 * @param length 长度
 * @param includeMath 是否包含数字
 */
fun genRandomStr(@IntRange(from = 0) length: Int = 6, includeMath: Boolean = true): String {
    val sb = StringBuilder()
    val charPool: MutableList<Char> = (('a'..'z') + ('A'..'Z')).toMutableList()
    if (includeMath) {
        charPool += ('0'..'9')
    }
    repeat(length) {
        val index = Random.nextInt(0, charPool.size)
        sb.append(charPool.getOrNull(index) ?: '0')
    }
    return sb.toString()
}