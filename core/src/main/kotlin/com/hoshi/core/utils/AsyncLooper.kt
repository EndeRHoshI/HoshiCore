package com.hoshi.core.utils

import android.os.Handler
import android.os.Looper

/**
 * 目前主要是用于快捷调用主线程，学习用，后续可以选择用别的替代方案
 */
object AsyncLooper {

    /**
     * Main thread handler.
     */
    @JvmStatic
    val handler by lazy { Handler(Looper.getMainLooper()) }

    fun main(runnable: Runnable) {
        handler.post(runnable)
    }

}