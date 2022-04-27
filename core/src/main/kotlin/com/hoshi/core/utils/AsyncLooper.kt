package com.hoshi.core.utils

import android.os.Handler
import android.os.Looper

object AsyncLooper {

    /**
     * Main thread handler.
     */
    @JvmStatic
    val handler by lazy { Handler(Looper.getMainLooper()) }

    fun main(action: () -> Unit) {
        handler.post(action)
    }

}