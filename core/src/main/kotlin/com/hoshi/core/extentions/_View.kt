package com.hoshi.core.extentions

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 隐藏软键盘
 */
fun View.hideKeyboard() {
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * 开始上下翻转动画
 * @param isUp 是否向上
 * @param duration 时长
 */
fun View.startUpDownAnim(isUp: Boolean, duration: Long = 240) {
    startAnimation(
        RotateAnimation(
            isUp.matchTrue(0F, 180F),
            isUp.matchTrue(180F, 0F),
            Animation.RELATIVE_TO_SELF,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F
        ).apply {
            this.fillAfter = true // 设置保持动画最后状态
            this.duration = duration // 设置动画时间
            this.interpolator = LinearInterpolator() // 设置插值器
        }
    )
}

/**
 * EditText 自动适配范围
 */
fun EditText.adjustRange(start: Int, end: Int) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val inputContent = s.toString()
            if (inputContent.isNotEmpty()) {
                if (inputContent.toFloat().toInt() > end) {
                    this@adjustRange.setText(end.toString())
                    this@adjustRange.setSelection(end.toString().length)
                }
                if (inputContent.toFloat().toInt() < start) {
                    this@adjustRange.setText(start.toString())
                    this@adjustRange.setSelection(start.toString().length)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}