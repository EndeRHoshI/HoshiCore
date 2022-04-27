package com.hoshi.core.extentions

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

fun <T> MediatorLiveData<T>.addMultiSource(vararg liveData: MutableLiveData<*>, onChange: () -> Unit) {
    liveData.forEach {
        addSource(it) {
            onChange.invoke()
        }
    }
}