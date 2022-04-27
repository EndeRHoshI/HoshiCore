package com.hoshi.core.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

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

}