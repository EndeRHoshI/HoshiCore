package com.hoshi.core.utils

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * 状态栏工具类
 */
object StatusBarUtils {

    /**
     * [isDark]表示当前主题的颜色是否是深色
     * 如果[isDark] == true , 文字设置为亮色
     * 如果[isDark] == false, 文字设置为深色
     */
    fun switchStatusBarTextColorCompat(window: Window, isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(View.SYSTEM_UI_FLAG_VISIBLE)
            window.decorView.systemUiVisibility =
                if (isDark) View.SYSTEM_UI_FLAG_VISIBLE else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /**
     * 比如 dialogFragment 中，setStatusBarColor 不会生效
     * 这个时候可以用半透明状态栏
     * 可能会影响到底部 navigationBar 的显示
     */
    fun setExtraStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winParams = window.attributes
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.attributes = winParams
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }
}