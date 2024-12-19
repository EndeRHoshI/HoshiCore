package com.hoshi.core.utils

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate

object DisplayUtils {

    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 获取屏幕的宽高
     *
     * @param context
     * @return
     */
    fun getScreenSize(context: Context): Pair<Int, Int> {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val height = dm.heightPixels
        val width = dm.widthPixels
        return Pair(width, height)
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context) = getScreenSize(context).first

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(context: Context) = getScreenSize(context).second


    fun getUiMode() = AppCompatDelegate.getDefaultNightMode()

    /**
     * 当前是否暗色，注意，这个只是反应是亮/暗，不是指模式，模式有更多，详见 AppCompatDelegate.getDefaultNightMode()
     * @param context Context
     * @return Boolean
     */
    fun isNightMode(context: Context) =
        context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    /**
     * 切换亮暗模式
     * @param context Context
     */
    fun switchNightMode(context: Context) {
        if (isNightMode(context)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    /**
     * 亮暗模式跟随系统
     */
    fun followSystemMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

}