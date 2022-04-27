package com.hoshi.core.extentions

import com.hoshi.core.const.valueUnset
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

// TODO 区分数字 format 和文本 format 到不同的扩展类中，同时写个 demo，每种方法都用一下，熟悉原理和效果

fun String?.toDoubleOrDefault(default: Double = 0.0): Double {
    return this?.toDoubleOrNull() ?: default
}

fun String?.toFloatOrDefault(default: Float = 0f): Float {
    return this?.toFloatOrNull() ?: default
}

/**
 * 按[decimal]用[roundingMode]进行取整
 */
fun Double.roundDecimal(decimal: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal {
    return this.toBigDecimal().setScale(decimal, roundingMode)
}

/**
 * 按[decimal]用[roundingMode]进行取整
 */
fun Float.roundDecimal(decimal: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal {
    return this.toBigDecimal().setScale(decimal, roundingMode)
}

fun Double.toDecimalString(decimal: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): String {
    return roundDecimal(decimal, roundingMode).toPlainString()
}

fun Float.toDecimalString(decimal: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): String {
    return roundDecimal(decimal, roundingMode).toPlainString()
}

fun Double.toDecimalStringDown(decimal: Int): String {
    return roundDecimal(decimal, RoundingMode.DOWN).toPlainString()
}


fun String.roundingZeros(
    maxDigits: Int,
    minDigits: Int = 0,
    roundingMode: RoundingMode = RoundingMode.HALF_UP,
    isGroupingUsed: Boolean? = null
): String {
    return toDoubleOrDefault().roundingZeros(maxDigits, minDigits, roundingMode, isGroupingUsed)
}

fun Double.roundingZeros(
    maxDigits: Int,
    minDigits: Int = 0,
    roundingMode: RoundingMode = RoundingMode.HALF_UP,
    isGroupingUsed: Boolean? = null
): String {
    return NumberFormat.getInstance().apply {
        maximumFractionDigits = maxDigits
        minimumFractionDigits = minDigits
        isGroupingUsed?.let { this.isGroupingUsed = it }
        this.roundingMode = roundingMode
    }.format(this)
}

/**
 * 四舍五入
 */
fun Double.formatDecimal(decimal: Int): String {
    return String.format("%.${decimal}f", this)
}

fun Double.formatDecimal(decimal: Int, mode: RoundingMode = RoundingMode.HALF_UP): String {
    return this.toDecimalString(decimal, mode)
}

fun String?.formatDecimal(decimal: Int, mode: RoundingMode = RoundingMode.HALF_UP): String {
    return this?.toDoubleOrNull()?.toDecimalString(decimal, mode) ?: valueUnset
}

/* -------- 避免科学计数法 ----------*/

fun Double?.toPlainString(): String {
    return try {
        this?.toBigDecimal()?.toPlainString() ?: "0"
    } catch (e: Exception) {
        "0"
    }
}

fun String?.toPlainString(default: String = "0"): String {
    return try {
        this?.toBigDecimal()?.toPlainString() ?: default
    } catch (e: Exception) {
        default
    }
}

/* -------- 格式化百分比 ----------- */

/** 1.87873 -> 187.87% */
fun String.formatPercentage(decimal: Int = 2): String {
    return (this.toDoubleOrNull()
        ?.times(100)
        ?.toDecimalString(decimal) ?: valueUnset) + "%"
}

/** 1.87873 -> 187.87% */
fun Double.formatPercentage(decimal: Int = 2): String {
    val originNum = if (this.isNaN()) 0.0 else this
    return (originNum.times(100).toDecimalString(decimal)) + "%"
}

/** 1.87873 -> 187.87% */
fun Float.formatPercentage(decimal: Int = 2): String {
    val originNum = if (this.isNaN()) 0.0F else this
    return (originNum.times(100).toDecimalString(decimal)) + "%"
}

/**
 * 没有乘以 100% 的百分比格式化
 */
fun Double.formatPercentageRaw(decimal: Int = 2): String {
    val originNum = if (this.isNaN()) 0.0 else this
    return (originNum.toDecimalString(decimal)) + "%"
}

/**
 * 没有乘以 100% 的百分比格式化
 */
fun String.formatPercentageRaw(decimal: Int = 2): String {
    return (this.toDoubleOrNull()
        ?.toDecimalString(decimal) ?: valueUnset) + "%"
}