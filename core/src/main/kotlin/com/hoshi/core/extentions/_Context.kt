package com.hoshi.core.extentions

import android.content.Context
import android.content.Intent
import com.hoshi.core.router.IntentBuilder
import com.hoshi.core.utils.HLog

fun Context.routeTo(action: String) {
    routeTo(Intent(action))
}

fun Context.routeTo(intent: Intent) {
    try {
        startActivity(intent)
    } catch (e: Exception) {
        HLog.e(e)
    }
}

fun Context.routeTo(intentBuilder: IntentBuilder) {
    routeTo(intentBuilder.buildIntent())
}