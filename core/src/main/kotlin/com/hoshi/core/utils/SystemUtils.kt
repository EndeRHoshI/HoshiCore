package com.hoshi.core.utils

import android.content.Context
import android.location.LocationManager
import android.os.Build
import java.util.*

/**
 * 系统工具类
 *
 * @author lv.qx on 2019/07/29
 */
object SystemUtils {

    /**
     * @return 手机厂商
     */
    fun getManufacturer(): String = Build.MANUFACTURER

    /**
     * @return 手机型号
     */
    fun getModel(): String = Build.MODEL

    /**
     * @return Android 系统版本号
     */
    fun getAndroidVersion(): String = Build.VERSION.RELEASE

    /**
     * @return 手机品牌
     */
    fun getBrand(): String = Build.BRAND

    /**
     * 获取手机系统版本号
     *
     * @return 系统版本号
     * @see Build.ID 可能和 Build.ID 一致
     */
    fun getDisplay(): String = Build.DISPLAY

    /**
     * 获取基带版本
     *
     * @return  基带版本，一般格式为 "angler-03.88"
     * 部分机型无法取得，显示 unknown
     */
    fun getRadio(): String = Build.RADIO

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
    fun getProduct(): String = Build.PRODUCT

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
    fun getHardware(): String = Build.HARDWARE

    /**
     * @return  CPU_ABI
     */
    fun getCpu(): String = Build.CPU_ABI

    /**
     * @return  SERIAL
     * 部分机型无法取得，显示 unknown
     */
    fun getSerial(): String = Build.SERIAL

    /**
     * @return  出厂时间
     */
    fun getTime() = Build.TIME

    /**
     * @return  HOST
     */
    fun getHost(): String = Build.HOST

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    fun getSystemLanguage(): String = Locale.getDefault().language

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    fun getSystemLanguageList(): Array<Locale> = Locale.getAvailableLocales()

    /**
     * 取得所有
     */
    fun getAll(): String {
        val sb = StringBuilder()
        val fields = Build::class.java.fields
        fields.forEach {
            try {
                val name = it.name
                val value = it.get(name)
                sb.append("name:")
                    .append(name)
                    .append(" value:")
                    .append(value)
                    .append("\n")
            } catch (e: IllegalAccessError) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有 app 将不能使用定位功能
     */
    fun isLocServiceEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

}