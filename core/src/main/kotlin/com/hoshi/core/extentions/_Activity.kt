package com.hoshi.core.extentions

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

/**
 * Created by lv.qx on 2024/9/27
 */

/**
 * 设置手机屏幕方向
 * @receiver Activity 基于 Activity 扩展
 * @param isLandscape Boolean 是否横屏
 * @param autoRotation Boolean 是否自动旋转
 */
fun Activity.setOrientation(isLandscape: Boolean, autoRotation: Boolean = false) {
    requestedOrientation = isLandscape.matchTrue(
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    )
    if (autoRotation) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }
}

/**
 * 取得当前是否横屏
 * @receiver Activity 基于 Activity 扩展
 * @return Boolean 是否横屏
 */
fun Activity.isLandscape(): Boolean = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE