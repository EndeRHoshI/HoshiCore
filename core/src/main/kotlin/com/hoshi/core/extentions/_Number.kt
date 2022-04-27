package com.hoshi.core.extentions

import android.content.res.Resources
import android.util.TypedValue

val Number.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

val Number.sp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()

/**
 * 规避除以零产生的异常
 */
inline infix fun <reified T : Number> T.safeDiv(divisor: T): T {
    return when (T::class) {
        Float::class -> {
            if (divisor == 0.0F) return 0F as T
            ((this as Float) / divisor.toFloat()) as T
        }
        Int::class -> {
            if (divisor == 0) return 0 as T
            ((this as Int) / divisor.toInt()) as T
        }
        Double::class -> {
            if (divisor == 0.0) return 0.0 as T
            ((this as Double) / divisor.toDouble()) as T
        }
        Long::class -> {
            if (divisor == 0L) return 0L as T
            ((this as Long) / divisor.toLong()) as T
        }
        else -> throw IllegalStateException("SafeDiv Type not supported")
    }
}