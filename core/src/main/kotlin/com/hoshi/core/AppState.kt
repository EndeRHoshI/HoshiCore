package com.hoshi.core

import android.content.Context
import java.lang.NullPointerException
import java.lang.ref.WeakReference

/**
 * AppState
 *
 * @author lv.qx at 2021/03/19
 */
object AppState {

    private var refContext: WeakReference<Context>? = null

    /**
     * 绑定 Application 的 Context，在 Application 的 onCreate 中调用
     *
     * @param context context.
     */
    @JvmStatic
    fun bindContext(context: Context) {
        refContext = WeakReference(context.applicationContext)
    }

    /**
     * 取得 Application 的 context
     */
    @JvmStatic
    fun getApplicationContext(): Context {
        val refContext = refContext
        if (refContext == null) {
            throw NullPointerException("please bind context in your application")
        } else {
            val context = refContext.get()
            if (context == null) {
                throw NullPointerException("can not get context, please check your code")
            } else {
                return context
            }
        }
    }

    /**
     * 取得 Application 的 context
     */
    @JvmStatic
    fun getPackageName(): String {
        return getApplicationContext().packageName ?: ""
    }

}