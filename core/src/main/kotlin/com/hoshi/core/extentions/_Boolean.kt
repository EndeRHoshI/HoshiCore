package com.hoshi.core.extentions

fun Boolean.whenTrue(action: () -> Unit) {
    if (this) {
        action.invoke()
    }
}

fun Boolean.whenFalse(action: () -> Unit) {
    if (!this) {
        action.invoke()
    }
}

fun <T> Boolean.matchTrue(trueObject: T, falseObject: T): T {
    return if (this) {
        trueObject
    } else {
        falseObject
    }
}