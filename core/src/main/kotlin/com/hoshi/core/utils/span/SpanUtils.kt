package com.hoshi.core.utils.span

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.util.regex.Pattern

/**
 * Span 工具类
 */
object SpanUtils {

    /**
     * 获得处理后的 SpannableString，添加点击事件，建议直接使用 SpanUtils.setClickSpan() 方法
     *
     * @param source    源文本
     * @param targetStrList 想要处理的文本列表
     * @param options   配置
     * @return SpannableString
     */
    fun getSpanStr(source: CharSequence, options: SpanOptions, targetStrList: List<String>): SpannableString {
        return getSpanStr(SpannableString(source), options, targetStrList)
    }

    /**
     * 获得处理后的 SpannableString，添加点击事件，建议直接使用 SpanUtils.setClickSpan() 方法
     *
     * @param source    源文本
     * @param targetStrList 想要处理的文本列表
     * @param options   配置
     * @return SpannableString
     */
    fun getSpanStr(source: SpannableString, options: SpanOptions, targetStrList: List<String>): SpannableString {
        return locateAndSpan(
            source,
            targetStrList
        ) { spannableString: SpannableString, startIndex: Int, endIndex: Int ->
            handleNativeSpan(
                spannableString,
                options,
                startIndex,
                endIndex
            )
        }
    }

    /**
     * 添加下划线
     * @param tv 目标 tv
     * @param hasUnderline 是否有下划线
     */
    fun setUnderline(
        tv: TextView,
        hasUnderline: Boolean = true
    ) {
        val textCharSequence = tv.text
        tv.text = getSpanStr(
            SpannableString(textCharSequence),
            SpanOptions.Builder()
                .setHasUnderline(hasUnderline)
                .build(),
            mutableListOf(textCharSequence.toString())
        )
    }

    /**
     * 设置带点击事件的 span，添加点击事件，直接使用此方法
     *
     * @param lifecycle 用于在生命周期处于销毁时，将 text 和 movementMethod 置空避免内存泄漏
     * @param tv TextView
     * @param options 配置
     * @param sourceStr 源文字，如为空，取 tv 的文字
     * @param highlightColor 点击事件后的高亮色
     */
    fun setClickSpan(
        lifecycle: Lifecycle,
        tv: TextView,
        options: SpanOptions = SpanOptions.Builder().build(),
        sourceStr: SpannableString = SpannableString(""),
        highlightColor: Int = Color.TRANSPARENT
    ) {
        setClickSpan(lifecycle, tv, options, sourceStr, highlightColor, mutableListOf(SpannableString(tv.text).toString()))
    }

    /**
     * 设置带点击事件的 span，添加点击事件，直接使用此方法
     *
     * @param lifecycle 用于在生命周期处于销毁时，将 text 和 movementMethod 置空避免内存泄漏
     * @param tv TextView
     * @param options 配置
     * @param sourceStr 源文字，如为空，取 tv 的文字
     * @param highlightColor 点击事件后的高亮色
     * @param targetStrList 想要处理的文本列表
     */
    fun setClickSpan(
        lifecycle: Lifecycle,
        tv: TextView,
        options: SpanOptions = SpanOptions.Builder().build(),
        sourceStr: String = "",
        highlightColor: Int = Color.TRANSPARENT,
        targetStrList: List<String>
    ) {
        setClickSpan(lifecycle, tv, options, SpannableString(sourceStr), highlightColor, targetStrList)
    }

    /**
     * 设置带点击事件的 span，添加点击事件，直接使用此方法
     *
     * @param lifecycle 用于在生命周期处于销毁时，将 text 和 movementMethod 置空避免内存泄漏
     * @param tv TextView
     * @param options 配置
     * @param sourceStr 源文字，如为空，取 tv 的文字
     * @param highlightColor 点击事件后的高亮色
     * @param targetStrList 想要处理的文本列表
     */
    fun setClickSpan(
        lifecycle: Lifecycle,
        tv: TextView,
        options: SpanOptions = SpanOptions.Builder().build(),
        sourceStr: SpannableString = SpannableString(""),
        highlightColor: Int = Color.TRANSPARENT,
        targetStrList: List<String>
    ) {
        // 取 SpannableString 而不是 String，以达到可以设置不同下划线跳转不同页面的效果
        val source = sourceStr.ifEmpty {
            SpannableString(tv.text)
        }

        fixClickSpan(lifecycle, tv, highlightColor)
        tv.text = getSpanStr(source, options, targetStrList)
    }

    /**
     * 统一处理 clickSpan 会出现的问题，包括必要的设置、高亮颜色、内存泄漏问题
     */
    private fun fixClickSpan(lifecycle: Lifecycle, tv: TextView, highlightColor: Int = Color.TRANSPARENT) {
        tv.movementMethod = LinkMovementMethod.getInstance()
        tv.highlightColor = highlightColor

        /*
         * 销毁时将 text 和 movementMethod 置空，修复 ClickableSpan 导致的内存泄漏问题
         * 1. 这个泄漏可能可以忽略不管
         * 2. 实现 NoCopySpan 修复可能导致使用辅助功能服务时出现问题（闪退）
         * 详见：https://stackoverflow.com/questions/28539216/android-textview-leaks-with-setmovementmethod
         */
        val lifecycleHandler = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                tv.text = ""
                tv.movementMethod = null
            }
        }
        lifecycle.removeObserver(lifecycleHandler)
        lifecycle.addObserver(lifecycleHandler)
    }

    /**
     * 获得处理后的 SpannableString
     *
     * @param source    源文本
     * @param targetStrList 想要处理的文本
     * @param color     颜色
     * @return SpannableString
     */
    fun getForeColorSpanStr(source: String, @ColorInt color: Int, targetStrList: List<String>): SpannableString {
        return getForeColorSpanStr(SpannableString(source), color, targetStrList)
    }

    /**
     * 获得处理后的 SpannableString
     *
     * @param source    源文本
     * @param targetStrList 想要处理的文本
     * @param color     颜色
     * @return SpannableString
     */
    fun getForeColorSpanStr(source: SpannableString, @ColorInt color: Int, targetStrList: List<String>): SpannableString {
        return getSpanStr(
            source,
            SpanOptions.Builder()
                .setForegroundSpanColor(color)
                .build(),
            targetStrList
        )
    }

    /**
     * 定位并执行 span，之后扩展的所有 span 方法，都通过该方法定位目标文字的位置，并且执行 action 中的逻辑进行 span
     *
     * @param source        源文本
     * @param targetStrList 目标文本列表
     * @param spanAction    实际进行 span 的操作
     * @return SpannableString
     */
    private fun locateAndSpan(
        source: SpannableString,
        targetStrList: List<String>,
        spanAction: (SpannableString, Int, Int) -> Unit
    ): SpannableString {
        if (targetStrList.isEmpty()) {
            return source
        }
        val startList: MutableList<Int> = ArrayList()
        val endList: MutableList<Int> = ArrayList()
        for (i in targetStrList.indices) {
            val targetStr = Pattern.quote(targetStrList[i]) // 先转化为文本，避免字符识别为正则
            val pattern = Pattern.compile(targetStr)
            val matcher = pattern.matcher(source)
            while (matcher.find()) {
                startList.add(matcher.start())
                endList.add(matcher.end())
            }
        }
        val startSize = startList.size
        val endSize = endList.size
        if (startSize != endSize || startSize == 0) {
            // 如果长度不相等或者有一个为 0
            return source
        }
        for (i in 0 until startSize) {
            val startIndex = startList[i]
            val endIndex = endList[i]

            // 开始处理各种 span
            spanAction.invoke(source, startIndex, endIndex)
        }
        return source
    }

    /**
     * 通过系统原生的各种 span 进行处理
     *
     * @param spannableString spannableString
     * @param options         配置
     * @param startIndex      起始
     * @param endIndex        终止
     */
    private fun handleNativeSpan(spannableString: SpannableString, options: SpanOptions, startIndex: Int, endIndex: Int) {
        val textSize = options.spanSize
        val backgroundSpanColor = options.backgroundSpanColor
        val foregroundSpanColor = options.foregroundSpanColor
        val typeface = options.typeface
        val clickListener = options.clickListener
        val hasUnderline = options.hasUnderline

        // 处理背景颜色
        if (backgroundSpanColor != null) {
            // 处理圆角，圆角需要背景色，如果没有背景色，不会设置圆角
            applySpan(spannableString, BackgroundColorSpan(backgroundSpanColor), startIndex, endIndex)
        }

        // 处理字号和字体，如果放在 updateDrawState 里面，会导致显示不全，因为需要测量，所以需要另外处理
        // 处理字号
        if (textSize != null) {
            applySpan(spannableString, AbsoluteSizeSpan(textSize), startIndex, endIndex)
        }

        // 处理粗体、斜体等
        if (typeface != null) {
            applySpan(spannableString, StyleSpan(typeface.style), startIndex, endIndex)
        }

        applySpan(
            spannableString,
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    clickListener?.onClick(widget) // 处理点击事件
                }

                override fun updateDrawState(@NonNull ds: TextPaint) {
                    // 处理字体颜色
                    if (foregroundSpanColor != null) {
                        ds.color = foregroundSpanColor
                    }

                    // 处理下划线
                    if (hasUnderline != null) {
                        ds.isUnderlineText = hasUnderline
                    }
                }
            },
            startIndex,
            endIndex
        )
    }

    /**
     * 将 span 设置到 spannableString 上
     *
     * @param spannableString spannableString
     * @param span            将要设置的 span
     * @param startIndex      起始
     * @param endIndex        终止
     */
    private fun applySpan(spannableString: SpannableString, span: Any, startIndex: Int, endIndex: Int) {
        spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}