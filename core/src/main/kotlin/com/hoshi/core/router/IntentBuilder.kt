package com.hoshi.core.router

import android.content.Intent

/**
 * 路由 Builder
 */
class IntentBuilder(createIntent: () -> Intent) {

    private val innerIntent = createIntent()

    /**
     * 添加 NEW_TASK flag
     */
    fun asNewTask(): IntentBuilder {
        innerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return this
    }

    /**
     * 添加 CLEAR_TOP flag
     */
    fun asClearTop(): IntentBuilder {
        innerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return this
    }

    /**
     * 对 Intent 进行额外的操作
     */
    fun apply(action: (Intent) -> Unit): IntentBuilder {
        action.invoke(innerIntent)
        return this
    }

    fun buildIntent(): Intent {
        return innerIntent
    }

}