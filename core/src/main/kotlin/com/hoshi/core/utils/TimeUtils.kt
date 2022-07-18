package com.hoshi.core.utils

import com.hoshi.core.extentions.orDefault
import com.hoshi.core.extentions.whenTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    val TIMEZONE_CHINA: TimeZone by lazy { TimeZone.getTimeZone("GMT+8") }

    fun getSimpleDateFormat(
        pattern: String,
        locale: Locale = Locale.getDefault()
    ) = SimpleDateFormat(pattern, locale).apply {
        timeZone = TIMEZONE_CHINA
    }

    /**
     * 倒计时，注意 onCompletion 除了在倒计时结束时调用，也会在生命周期结束时调用，如果有要求，需要另外处理
     * @param onTick 每次倒计时要做的，例如更新时间
     * @param onFinish 倒计时完成后要做的
     * @param total 倒计时总次数，tick 次数
     * @param tickMillis 每一 tick 的间隔
     * @param scope 作用域，选取合适的，避免泄漏
     */
    fun countDown(
        total: Int,
        onTick: (Int) -> Unit,
        onFinish: () -> Unit,
        tickMillis: Long = 1000,
        scope: CoroutineScope = MainScope()
    ) {
        flow {
            for (i in total downTo 1) {
                emit(i)
                delay(tickMillis)
            }
        }.flowOn(Dispatchers.Default)
            .onCompletion { onFinish.invoke() }
            .onEach { onTick.invoke(it) }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)
    }

    /**
     * 计时器，注意 onCompletion 除了在倒计时结束时调用，也会在生命周期结束时调用，如果有要求，需要另外处理
     * @param onTick 每次倒计时要做的，例如更新时间
     * @param onFinish 倒计时完成后要做的
     * @param total 默认是Int的最大值，tick 次数
     * @param tickMillis 每一 tick 的间隔
     * @param scope 作用域，选取合适的，避免泄漏
     */
    fun timeSchedule(
        total: Int = Int.MAX_VALUE,
        onTick: (Int) -> Unit,
        onFinish: () -> Unit,
        tickMillis: Long = 1000,
        scope: CoroutineScope = MainScope()
    ): Job {
        return flow {
            for (i in 0 until total) {
                emit(i)
                delay(tickMillis)
            }
        }.flowOn(Dispatchers.Default)
            .onCompletion { onFinish.invoke() }
            .onEach { onTick.invoke(it) }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)
    }

    /**
     * 秒转换为分钟的形式 00:00
     */
    fun getSecondsToMinutes(number: Int): String {
        val minutes = number / 60
        val seconds = number % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * 毫秒转换为分秒的形式 00:00
     */
    @JvmStatic
    fun getMillisToMinSec(millis: Long): String {
        return getFormattedTime(millis, "mm:ss")
    }

    @JvmStatic
    @JvmOverloads
    fun getFormattedTime(timeMillis: Long?, pattern: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()): String {
        timeMillis ?: return ""
        val sdf = getSimpleDateFormat(pattern, locale)
        sdf.timeZone = TIMEZONE_CHINA
        return sdf.format(Date(timeMillis))
    }

    /**
     * 获取一天中开始的时刻
     * @param timeMillis 某个时间
     */
    fun getStartTimeOfOneDay(timeMillis: Long): Long {
        val cal = Calendar.getInstance(TIMEZONE_CHINA)
        cal.time = Date(timeMillis)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * 获取一天中最后的时刻
     * @param timeMillis 某个时间
     */
    fun getEndTimeOfOneDay(timeMillis: Long): Long {
        val cal = Calendar.getInstance(TIMEZONE_CHINA)
        cal.time = Date(timeMillis)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    /**
     * 获取下一天的时刻
     */
    fun getNextDayMs(timeMillis: Long): Long {
        val cal = Calendar.getInstance(TIMEZONE_CHINA)
        cal.timeInMillis = timeMillis
        cal.add(Calendar.DAY_OF_MONTH, 1) // 加一天
        return cal.timeInMillis
    }

    /**
     * 获取前一天的时刻
     */
    fun getPreDayMs(timeMillis: Long): Long {
        val cal = Calendar.getInstance(TIMEZONE_CHINA)
        cal.timeInMillis = timeMillis
        cal.add(Calendar.DAY_OF_MONTH, -1) // 减一天
        return cal.timeInMillis
    }

    /**
     * 获取月的开始时间
     */
    fun getStartTimeOfMonth(timeMillis: Long, amount: Int = 0): Long {
        val cal = Calendar.getInstance(TIMEZONE_CHINA)
        cal.timeInMillis = timeMillis
        //前几个月/后几个月
        cal.add(Calendar.MONTH, amount)
        //获取到月的起始日
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * 获取月的结束时间
     */
    fun getEndTimeOfMonth(timeMillis: Long, amount: Int = 0): Long {
        val cal = Calendar.getInstance(TIMEZONE_CHINA)
        cal.timeInMillis = timeMillis
        //前几个月/后几个月
        cal.add(Calendar.MONTH, amount)
        //获取到月的结束日
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    /**
     * 获取当前分钟时间戳，去除秒和毫秒
     */
    fun getCurrentMin(): Long {
        val currentTime = System.currentTimeMillis()
        val cal = Calendar.getInstance(TIMEZONE_CHINA)
        cal.time = Date(currentTime)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getPetAge(birthTimeMillis: Long?): String {
        birthTimeMillis ?: return "0天"

        val oneYear = 365
        val currentTimeMillis = System.currentTimeMillis()

        val diff = currentTimeMillis - birthTimeMillis
        val totalDayNum = (diff / DAY).toInt()
        val yearNum = totalDayNum / oneYear
        val dayNum = totalDayNum % oneYear

        val result = StringBuilder()
        if (yearNum != 0) {
            result.append(yearNum).append("岁")
        }
        if (dayNum != 0) {
            result.append(dayNum).append("天")
        }

        return result.toString().orDefault("0天")
    }

}