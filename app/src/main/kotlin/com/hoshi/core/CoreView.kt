package com.hoshi.core

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import androidx.annotation.FloatRange
import androidx.core.view.doOnAttach
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.hoshi.core.extentions.dp
import com.hoshi.core.utils.HLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlin.math.max
import kotlin.math.min

/**
 * 拙劣丑陋的旋转动画，加减速效果比较差
 * Created by lv.qx on 2022/4/29
 */
class CoreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val rotateAnim by lazy { InfinityRotateAnim() } // 无限旋转动画

    init {
        startAnimation(rotateAnim) // 开启旋转动画

        // attachToWindow 之后才能使用 findViewTreeLifecycleOwner，否则因为未 attach 而返回为 null
        doOnAttach {
            // 快速地获取一个最近的 Fragment 或者 Activity 的 LifecycleOwner
            findViewTreeLifecycleOwner()?.lifecycleScope?.let {
                // 打开一个 Flow，无限循环，用来监听按下和抬起
                flow {
                    while (true) {
                        emit(null)
                        factor = if (isPress) {
                            // 如果按下，增大因子，即加速
                            min(factor + 0.02F, maxFactor)
                        } else {
                            // 如果抬起，减小因子，即减速
                            max(factor - 0.02F, minFactor)
                        }
                        HLog.d("factor = $factor")
                        delay(10) // 执行间隔为 10 毫秒
                    }
                }.flowOn(Dispatchers.Default)
                    .launchIn(it)
            }
        }
    }

    private val circleWidth by lazy { 40.dp }
    private var currentDegree = 0F

    @FloatRange(from = 1.0, to = 5.0)
    private var maxFactor = 5F
    private var minFactor = 1F
    private var factor = 1F
    private var isPress = false

    private val commonPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = circleWidth.toFloat()
        }
    }
    private val outerPaint by lazy { Paint(commonPaint).apply { color = Color.RED } }
    private val innerPaint by lazy { Paint(commonPaint).apply { color = Color.GREEN } }
    private val defaultSize = 100.dp // 自定义 View 默认的宽高

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measureSize(defaultSize, heightMeasureSpec)
        val width = measureSize(defaultSize, widthMeasureSpec)
        val min = min(width, height) // 获取 View 最短边的长度
        setMeasuredDimension(min, min) // 强制改 View 为以最短边为长度的正方形
    }

    private fun measureSize(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = min(result, specSize)
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height
        val halfW = width / 2F
        val halfH = height / 2F
        val outerRadius = halfH - circleWidth / 2
        val innerRadius = outerRadius - circleWidth
        canvas.drawCircle(width / 2F, halfH, outerRadius, outerPaint)
        canvas.drawCircle(width / 2F, halfH, innerRadius, innerPaint)

        canvas.save()
        canvas.translate(halfW, halfH)
        canvas.rotate(currentDegree)
        repeat(12) {
            canvas.drawLine(halfW - 20.dp, 0F, halfW, 0F, commonPaint)
            canvas.rotate(30F)
        }
        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        isPress = when (event?.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> true
            else -> false
        }
        return true
    }

    /**
     * 旋转动画
     */
    private inner class InfinityRotateAnim : Animation() {

        init {
            repeatCount = ValueAnimator.INFINITE
            duration = 2000
            interpolator = LinearInterpolator()
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            currentDegree = 360 * interpolatedTime * factor
            postInvalidate()
        }

    }

}