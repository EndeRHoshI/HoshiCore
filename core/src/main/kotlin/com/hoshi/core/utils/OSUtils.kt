package com.hoshi.core.utils

import android.os.Build

object OSUtils {

    private const val TAG = "OSUtils"
    private const val KEY_EMUI_VERSION_NAME = "ro.build.version.emui"

    private fun getSystemProperty(key: String, defaultValue: String = ""): String {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val get = clz.getMethod("get", String::class.java, String::class.java)
            return get.invoke(clz, key, defaultValue)?.toString() ?: defaultValue
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

    /**
     * 判断是否为emui
     * Is emui boolean.
     *
     * @return the boolean
     */
    fun isEMUI(): Boolean {
        val property: String = getSystemProperty(KEY_EMUI_VERSION_NAME)
        return property.isNotEmpty()
    }

    /**
     * 得到emui的版本
     * Gets emui version.
     *
     * @return the emui version
     */
    fun getEMUIVersion(): String {
        return if (isEMUI()) getSystemProperty(KEY_EMUI_VERSION_NAME) else ""
    }

    /**
     * 判断 emui 系统是否大于等于某个版本
     *
     * @param targetVersion 目标版本
     */
    fun isEMUIHigherThan(targetVersion: Int): Boolean {
        return try {
            val property: String = getEMUIVersion()
            if (property.isEmpty()) {
                val versionStr: String
                var version = 0.0
                // EMotionUI_x.x
                val arr: Array<String> = if (property.contains("_")) {
                    property.split("_").toTypedArray()
                } else {
                    property.split(" ").toTypedArray()
                }
                if (arr.size >= 2) {
                    versionStr = getEMUIVersion(arr[1])
                    if (!versionStr.isEmpty()) {
                        version = versionStr.toDouble()
                    }
                }
                return version >= targetVersion
            }
            false
        } catch (e: Exception) {
            HLog.v(TAG, "error:$e")
            false
        }
    }

    private fun getEMUIVersion(version: String?): String {
        if (version.isNullOrEmpty()) {
            return ""
        }
        if (version.contains(".")) {
            val arr = version.split("\\.").toTypedArray()
            if (arr.size >= 2) {
                return arr[0] + "." + arr[1]
            }
        }
        return ""
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun getModel(): String {
        return Build.MODEL
    }

    /**
     * 获取 Android 系统版本号
     *
     * @return  Android 系统版本号
     */
    fun getAndroidVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机品牌
     *
     * @return  手机品牌
     */
    fun getBrand(): String {
        return Build.BRAND
    }

    /**
     * 获取手机系统版本号
     *
     * @return  系统版本号
     * @see Build.ID 可能和 Build.ID 一致
     */
    fun getDisplay(): String {
        return Build.DISPLAY
    }

    /**
     * 获取基带版本
     *
     * @return  基带版本，一般格式为 "angler-03.88"
     * 部分机型无法取得，显示 unknown
     */
    fun getRadio(): String {
        return Build.getRadioVersion()
    }

    /**
     * 获取手机系统内核源代码代号
     *
     * @return  系统内核源代码代号
     * 可能和以下参数一致
     * @see Build.BOOTLOADER
     * @see Build.RADIO 基带版本
     * @see Build.ID
     * @see Build.DEVICE
     * @see Build.HARDWARE
     */
    fun getProduct(): String {
        return Build.PRODUCT
    }

    /**
     * 获取手机硬件版本号
     *
     * @return  硬件版本号
     * 可能和以下参数一致
     * @see Build.PRODUCT
     * @see Build.BOOTLOADER
     * @see Build.RADIO
     * @see Build.ID
     * @see Build.DEVICE
     */
    fun getHardware(): String {
        return Build.HARDWARE
    }

    /**
     * 获取 CPU_ABI
     *
     * @return  CPU_ABI
     */
    fun getCpu(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS.toMutableList().toString()
        } else {
            Build.CPU_ABI
        }
    }

    /**
     * 获取 SERIAL
     *
     * @return  SERIAL
     * 部分机型无法取得，显示 unknown
     */
    fun getSerial(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            "版本高于 8.0，可能需要权限" // android.permission.READ_PRIVILEGED_PHONE_STAT
        } else {
            Build.SERIAL
        }
    }

    /**
     * 获取出厂时间
     *
     * @return  出厂时间
     */
    fun getTime(): Long {
        return Build.TIME
    }
}