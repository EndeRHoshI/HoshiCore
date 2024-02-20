package com.hoshi.core.utils

import android.os.Build
import android.text.TextPaint
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.TextViewCompat
import com.hoshi.core.extentions.sp

/**
 * 文本处理工具类
 *
 * @author lv.qx at 2021/03/19
 */
object TextUtils {

    /**
     * 分享文字时，中文算一个字符，英文算半个字符，通过这个方法算出当前已经输入字符数，向上取整
     *
     * @param content 文本内容
     * @return 字符数，向上取整，如：13.5 = 14
     */
    fun getShareTextNum(content: String): Int {
        val halfNum: Int = getHalfCharNum(content)
        val fullNum = content.length - halfNum
        return fullNum + (halfNum * 0.5 + 0.5).toInt()
    }

    /**
     * 获得半字符的个数，参考微博得出的逻辑，半字符指 Unicode 编码 0x0020 号到 0x007E 号之间的字符
     * @param contentStr 文本内容
     * @return 半字符的个数
     */
    fun getHalfCharNum(contentStr: String): Int {
        var num = 0
        val contentCharArray = contentStr.toCharArray()
        contentCharArray.forEach {
            if (it.toInt() in 0x0020..0x007E) {
                num++
            }
        }
        return num
    }

    /**
     * 获取中文字符的数量，如何界定中文字符，还需要再商榷
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @Deprecated("还要考虑下怎样写才对")
    fun getChineseCharNum(contentStr: String): Int {
        var num = 0
        val contentCharArray = contentStr.toCharArray()
        contentCharArray.forEach {
            if (isChinesePunctuation(it) || isChineseWord(it)) {
                num++
            }
        }
        return num
    }

    /**
     * 根据 UnicodeBlock 方法判断是否中文标点符号
     * 参考：https://m.yisu.com/zixun/213091.html
     *
     * @param c 字符
     * @return 是否中文标点符号
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun isChinesePunctuation(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)

        // Character.UnicodeBlock.VERTICAL_FORMS 需要 Api 19，如果小于 19，直接为 false
        val isVerticalForms = AndroidVersionUtils.lessThanMatch(
            Build.VERSION_CODES.KITKAT,
            false,
            (ub == Character.UnicodeBlock.VERTICAL_FORMS)
        )
        return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION // U2000-General Punctuation (百分号，千分号，单引号，双引号等)
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION // U3000-CJK Symbols and Punctuation (顿号，句号，书名号，〸，〹，〺 等)
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS // UFF00-Halfwidth and Fullwidth Forms (大于，小于，等于，括号，感叹号，加，减，冒号，分号等等)
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS // UFE30-CJK Compatibility Forms (主要是给竖写方式使用的括号，以及间断线﹉，波浪线﹌等)
            || isVerticalForms // UFE10-Vertical Forms (主要是一些竖着写的标点符号，    等等)
    }

    /**
     * 如何界定中文字符，还需要再商榷
     */
    @Deprecated("还要考虑下怎样写才对")
    fun isChineseWord(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
    }

    /**
     * @param textPaint TextPaint
     * @return 使文字垂直居中所需的 Y
     */
    fun getTextHeight(textPaint: TextPaint): Float {
        val fontMetrics = textPaint.fontMetrics // 取得 fontMetrics
        return fontMetrics.bottom - fontMetrics.top // 计算文字高度
    }

    /**
     * @param viewHeight View 的高度
     * @param textPaint TextPaint
     * @return 使文字垂直居中所需的 Y
     */
    fun getTextCenterY(viewHeight: Float, textPaint: TextPaint): Float {
        val fontMetrics = textPaint.fontMetrics // 取得 fontMetrics
        val fontMetricsBottom = fontMetrics.bottom
        val fontH = fontMetricsBottom - fontMetrics.top // 计算文字高度
        return viewHeight - (viewHeight - fontH) / 2 - fontMetricsBottom
    }

    /**
     *
     * @param textView TextView 目标 TextView
     * @param autoSizeMaxTextSize Int 最大值
     * @param autoSizeMinTextSize Int 最小值
     * @param autoSizeStepGranularity Int  粒度值，即每次增量或减量的值
     * @param unit Int 单位
     */
    fun setTextViewAutoSize(
        textView: TextView,
        autoSizeMaxTextSize: Int = 14.sp,
        autoSizeMinTextSize: Int = 5.sp,
        autoSizeStepGranularity: Int = 1.sp,
        unit: Int = TypedValue.COMPLEX_UNIT_SP,
    ) {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM) // 以均衡缩放水平轴和垂直轴
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            textView,
            autoSizeMinTextSize,
            autoSizeMaxTextSize,
            autoSizeStepGranularity,
            unit
        )
    }

}