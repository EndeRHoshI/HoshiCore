package com.hoshi.core.extentions

/**
 * Created by lv.qx on 2023/11/21
 */

fun StringBuilder.newline(): StringBuilder {
    this.append("\n")
    return this
}