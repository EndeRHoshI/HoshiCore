package com.hoshi.core.utils

import com.hoshi.core.extentions.whenTrue
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    const val MILLI_SEC = 1L
    const val SEC = MILLI_SEC * 1000
    const val MIN = SEC * 60
    const val HOUR = MIN * 60
    const val DAY = HOUR * 24

    /**
     * 取得格式化字符串
     * @param timeMillis 要转换的时间
     */
    fun getFormattedTime(
        timeMillis: Long,
        format: String = "yyyy-MM-dd HH:mm:ss:SSS"
    ): String = SimpleDateFormat(format, Locale.getDefault()).format(timeMillis)

    /**
     * 取得 dead line 时间
     * @param endTime 结束时间，单位：毫秒
     */
    fun getDeadLineTime(endTime: Long, maxUnit: Long = DAY) = getDeadLineTime(System.currentTimeMillis(), endTime, maxUnit)

    /**
     * 取得 dead line 时间
     * @param startTime 开始时间，单位：毫秒
     * @param endTime 结束时间，单位：毫秒
     */
    fun getDeadLineTime(startTime: Long, endTime: Long, maxUnit: Long = DAY): String {

        var day = 0
        var hour = 0
        var min = 0
        var sec = 0

        val diffTime = endTime - startTime

        when (maxUnit) {
            SEC -> {
                if (diffTime < 0) return getCommonTimeFormat(sec)

                sec = (diffTime / SEC).toInt()

                return getCommonTimeFormat(sec)
            }
            MIN -> {
                if (diffTime < 0) return getCommonTimeFormat(sec, min)

                min = (diffTime / MIN).toInt()
                sec = ((diffTime % MIN) / SEC).toInt()

                return getCommonTimeFormat(sec, min)
            }
            HOUR -> {
                if (diffTime < 0) return getCommonTimeFormat(sec, min, hour)

                hour = (diffTime / HOUR).toInt()
                min = ((diffTime % HOUR) / MIN).toInt()
                sec = ((diffTime % MIN) / SEC).toInt()

                return getCommonTimeFormat(sec, min, hour)
            }
            DAY -> {
                if (diffTime < 0) return getCommonTimeFormat(sec, min, hour, day)

                day = (diffTime / DAY).toInt()
                hour = ((diffTime % DAY) / HOUR).toInt()
                min = ((diffTime % HOUR) / MIN).toInt()
                sec = ((diffTime % MIN) / SEC).toInt()

                return getCommonTimeFormat(sec, min, hour, day)
            }
            else -> return getCommonTimeFormat(sec, min, hour, day)
        }
    }

    fun getCommonTimeFormat(
        sec: Int? = null,
        min: Int? = null,
        hour: Int? = null,
        day: Int? = null
    ): String {
        val sb = StringBuilder()

        (day != 0 && day != null).whenTrue { sb.append(day).append("天") }
        (hour != 0 && hour != null).whenTrue { sb.append(hour).append("时") }
        (min != 0 && min != null).whenTrue { sb.append(min).append("分") }
        (sec != 0 && sec != null).whenTrue { sb.append(sec).append("秒") }

        return sb.toString()
    }
}