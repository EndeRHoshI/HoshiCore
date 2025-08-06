package com.hoshi.core.utils

import android.app.Activity
import android.content.Context

/**
 * App 相关的工具类
 * Created by lv.qx on 2024/8/12
 */
object AppUtils {

    /**
     * 检测指定应用是否安装
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        runCatching {
            context.packageManager.getPackageInfo(packageName, 0) // 如果应用程序不存在，会抛出 PackageManager.NameNotFoundException 异常
        }.onFailure { return false }
        return true
    }

    /**
     * 获取活动名
     */
    fun getActName(activity: Activity): String {
        val fullName = activity.localClassName
        return fullName.substring(fullName.lastIndexOf(".") + 1)
    }

}