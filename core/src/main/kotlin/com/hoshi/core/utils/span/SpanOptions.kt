package com.hoshi.core.utils.span

import android.graphics.Typeface
import android.view.View
import androidx.annotation.ColorInt

class SpanOptions private constructor(builder: Builder) {

    @ColorInt
    var foregroundSpanColor: Int? = null // 字体颜色
    @ColorInt
    var backgroundSpanColor: Int? = null // 字体背景颜色
    var spanSize: Int? = null // 字号
    var bgRadius: Int? = null // 背景圆角
    var typeface: Typeface? = null // 字体样式
    var clickListener: View.OnClickListener? = null
    var hasUnderline: Boolean? = null

    init {
        foregroundSpanColor = builder.foregroundSpanColor
        backgroundSpanColor = builder.backgroundSpanColor
        spanSize = builder.textSize
        bgRadius = builder.bgRadius
        typeface = builder.typeface
        clickListener = builder.clickListener
        hasUnderline = builder.hasUnderline
    }

    class Builder {
        @ColorInt
        internal var foregroundSpanColor: Int? = null // 字体颜色

        @ColorInt
        internal var backgroundSpanColor: Int? = null // 字体背景颜色
        internal var textSize: Int? = null
        internal var bgRadius: Int? = null // 背景圆角
        internal var typeface: Typeface? = null // 字体样式
        internal var hasUnderline: Boolean? = null // 字体样式
        internal var clickListener: View.OnClickListener? = null // 点击事件

        fun setForegroundSpanColor(@ColorInt foreGroundSpanColor: Int?): Builder {
            foregroundSpanColor = foreGroundSpanColor
            return this
        }

        fun setBackgroundSpanColor(@ColorInt backgroundSpanColor: Int?): Builder {
            this.backgroundSpanColor = backgroundSpanColor
            return this
        }

        fun setSpanSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        fun setBgRadius(bgRadius: Int): Builder {
            this.bgRadius = bgRadius
            return this
        }

        fun setHasUnderline(hasUnderline: Boolean?): Builder {
            this.hasUnderline = hasUnderline
            return this
        }

        /**
         * @param typeface NORMAL, BOLD, ITALIC, BOLD_ITALIC
         * @see Typeface
         *
         * @return builder
         */
        fun setTypeface(typeface: Typeface?): Builder {
            this.typeface = typeface
            return this
        }

        /**
         * 添加点击事件，注意使用这个需要调用 setMovementMethod，且可能导致内存泄漏，建议直接使用 SpanUtils.setClickSpan() 方法
         * @param clickListener
         * @return builder
         */
        fun setClickListener(clickListener: View.OnClickListener?): Builder {
            this.clickListener = clickListener
            return this
        }

        fun build(): SpanOptions {
            return SpanOptions(this)
        }
    }
}