package com.hoshi.core.startup

import android.content.Context
import androidx.startup.Initializer
import com.hoshi.core.AppState

/**
 * 统一处理初始化
 * Created by lv.qx on 2022/5/11
 */
class StartupLibInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        AppState.bindContext(context)
    }

    override fun dependencies() = mutableListOf<Class<out Initializer<*>>>()

}