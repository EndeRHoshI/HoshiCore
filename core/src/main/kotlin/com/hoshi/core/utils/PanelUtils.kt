package com.hoshi.core.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.hoshi.core.AppState

/**
 * Android Q 及以上机型使用 Panel 工具类
 */
object PanelUtils {
    private const val TAG = "PanelUtils"

    /**
     * 弹出 WiFi 控制面板
     */
    @SuppressLint("InlinedApi")
    fun showWiFi() {
        showPanel(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
    }

    /**
     * 弹出音量控制面板
     */
    @SuppressLint("InlinedApi")
    fun showVolume() {
        showPanel(Settings.Panel.ACTION_VOLUME)
    }

    private fun showPanel(panelType: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppState.getApplicationContext().startActivity(
                Intent(panelType).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } else {
            lowerThanQLog()
        }
    }

    private fun lowerThanQLog() {
        HLog.i(TAG, "低于 Android Q 无法使用 Panel")
    }
}