package com.hoshi.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Process
import android.telephony.TelephonyManager
import java.util.Locale
import kotlin.system.exitProcess

/**
 * 系统工具类
 *
 * @author lv.qx on 2019/07/29
 */
object SystemUtils {

    /**
     * 取得当前 HoshiCore 库的版本，暂时手写放在这里，后续看看要怎么弄比较好
     * @return String
     */
    fun getCoreVersion() = "1.0.1"

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
    fun getRadio(): String = Build.getRadioVersion()

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
    fun getCpu(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        Build.SUPPORTED_ABIS.toMutableList().toString()
    } else {
        Build.CPU_ABI
    }

    /**
     * @return  SERIAL
     * 部分机型无法取得，显示 unknown
     */
    fun getSerial(): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        "版本高于 8.0，可能需要权限" // android.permission.READ_PRIVILEGED_PHONE_STAT
    } else {
        Build.SERIAL
    }

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

    /**
     * 跳转到应用详情界面
     */
    fun gotoAppDetailIntent(context: Context) {
        context.startActivity(
            Intent().apply {
                action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.parse("package:" + context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    /**
     * @return Boolean 是否存在 SDCard
     */
    private fun isSDCardExist() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /**
     * 检查是否有 sim 卡
     * @param context Context
     * @return Boolean 否有 sim 卡
     */
    fun hasSimCard(context: Context): Boolean {
        val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.simState
        return simState != TelephonyManager.SIM_STATE_ABSENT && simState != TelephonyManager.SIM_STATE_UNKNOWN  // 当处于这两种状态下时，就是没有 SIM 卡
    }

    fun killApp(activity: Activity) {
        // 调用 Activity 类的 finishAffinity() 方法关闭任务栈中所有 Activity 界面
        // 只调用 finish 可能有些版本还是会重启，做吉利车机项目时，用 finish 配合 System.exit(status) 方法杀不掉 app，还是会拉起，暂时没测试出根本原因
        activity.finishAffinity()

        Process.killProcess(Process.myPid()) // 使用 Android 的进程 api 类 `android.os.Process` 杀死进程

        exitProcess(0) // 使用 Java 的进程 api 类 `java.lang.System` 杀死进程
    }

}