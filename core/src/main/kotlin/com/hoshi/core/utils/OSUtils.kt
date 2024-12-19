package com.hoshi.core.utils

/**
 * 操作系统工具类，与 System 的区别是，这个工具类主要负责各个不同厂商系统的不同工具方法，比如 EMUI、小米系统、魅族 Flyme、一加 ColorOS 等这些
 */
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
     * 判断是否为 emui
     * Is emui boolean.
     *
     * @return the boolean
     */
    fun isEMUI(): Boolean {
        val property = getSystemProperty(KEY_EMUI_VERSION_NAME)
        return property.isNotEmpty()
    }

    /**
     * 得到 emui 的版本
     * Gets emui version.
     *
     * @return the emui version
     */
    fun getEMUIVersion() = if (isEMUI()) getSystemProperty(KEY_EMUI_VERSION_NAME) else ""

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

}