package com.hoshi.core.utils

import android.util.Log

object HLog {

    private const val DEFAULT_TAG = "hoshiTest"

    @JvmStatic
    fun v(tag: String, msg: String) {
        Log.v(tag, msg)
    }

    @JvmStatic
    fun v(msg: String) {
        Log.v(DEFAULT_TAG, msg)
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    @JvmStatic
    fun i(msg: String) {
        Log.i(DEFAULT_TAG, msg)
    }

    @JvmStatic
    fun e(msg: String) {
        e(DEFAULT_TAG, msg)
    }

    @JvmStatic
    fun e(exception: Throwable?) {
        e(DEFAULT_TAG, exception)
    }

    @JvmStatic
    fun e(tag: String, exception: Throwable?) {
        Log.e(tag, null, exception)
    }

    @JvmStatic
    fun e(tag: String, msg: String, exception: Throwable? = null) {
        Log.e(tag, msg, exception)
    }

    @JvmStatic
    fun d(msg: String) {
        d(DEFAULT_TAG, msg)
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    @JvmStatic
    fun onHttpEvent(message: String) {
        d("http_request", message)
    }
}