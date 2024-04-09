package com.hoshi.core.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

object ClipboardUtils {

    /**
     * 复制文本到剪贴板
     */
    @JvmStatic
    fun copyText(
        context: Context,
        text: String
    ) {
        val cm: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        cm?.setPrimaryClip(ClipData.newPlainText("text", text)) // 把数据复制到剪贴板
    }

    /**
     * 获取剪贴板的文本
     */
    @JvmStatic
    fun getText(context: Context): CharSequence {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = cm.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).coerceToText(context)
        } else ""
    }

    /**
     * 复制 uri 到剪贴板
     */
    fun copyUri(context: Context, uri: Uri?) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newUri(context.contentResolver, "uri", uri))
    }

    /**
     * 获取剪贴板的 uri
     *
     * @return 剪贴板的 uri
     */
    fun getUri(context: Context): Uri? {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = cm.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).uri
        } else null
    }


    /**
     * 复制意图到剪贴板
     *
     * @param intent 意图
     */
    fun copyIntent(context: Context, intent: Intent?) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newIntent("intent", intent))
    }

    /**
     * 获取剪贴板的意图
     *
     * @return 剪贴板的意图
     */
    fun getIntent(context: Context): Intent? {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = cm.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).intent
        } else null
    }

}