package com.hoshi.core.extentions

import android.content.Context
import android.content.Intent
import com.hoshi.core.router.IntentBuilder
import com.hoshi.core.utils.HLog

/**
 * 跳转到指定 Action 的意图
 * @receiver Context 上下文
 * @param action String 对应的 Action
 * @param packageName String 适配 Android 14（要启动非导出活动，您的应用程序应使用显式意图），不传则默认添加，传 null 或空字符串则不添加
 */
fun Context.routeTo(action: String, packageName: String? = getPackageName()) {
    routeTo(
        Intent(action).apply {
            if (!packageName.isNullOrEmpty()) {
                setPackage(packageName)
            }
        }
    )
}

/**
 * 跳转到指定的意图
 * @receiver Context 上下文
 * @param intent Intent 对应的 intent
 * @param packageName String 适配 Android 14（要启动非导出活动，您的应用程序应使用显式意图），不传则默认添加，传 null 或空字符串则不添加
 */
fun Context.routeTo(intent: Intent, packageName: String? = getPackageName()) {
    runCatching {
        if (!packageName.isNullOrEmpty()) {
            intent.setPackage(packageName)
        }
        startActivity(intent)
    }.onFailure {
        HLog.e(it)
    }
}

fun Context.routeTo(intentBuilder: IntentBuilder) {
    routeTo(intentBuilder.buildIntent())
}