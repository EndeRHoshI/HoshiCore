package com.hoshi.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

/**
 * 跳转工具类，跳转到各种第三方页面
 */
object JumpUtils {

    object PackageName {
        const val JD = "com.jingdong.app.mall"
    }

    /**
     * 直接拨打电话，需要 CALL_PHONE 权限
     *
     * @param phoneNum 电话号码
     */
    fun callPhone(context: Context, phoneNum: String) {
        val intent = Intent(Intent.ACTION_CALL)
        val data: Uri = Uri.parse("tel:$phoneNum")
        intent.data = data
        context.startActivity(intent)
    }

    /**
     * 跳转到拨号盘，需要 CALL_PHONE 权限
     *
     * @param phoneNum 电话号码
     */
    fun toDial(context: Context, phoneNum: String? = null) {
        val intent = Intent(Intent.ACTION_DIAL)
        phoneNum?.apply {
            val data: Uri = Uri.parse("tel:$this")
            intent.data = data
        }
        context.startActivity(intent)
    }

    /**
     * 跳转到拨号盘，需要 SEND_SMS 权限
     *
     * @param phoneNum 电话号码
     */
    fun smsTo(context: Context, phoneNum: String? = null, smsBody: String? = null) {
        val smsToUri = Uri.parse("smsto:")
        val intent = Intent(Intent.ACTION_VIEW, smsToUri)
        phoneNum?.let { intent.putExtra("address", it) }
        smsBody?.let { intent.putExtra("sms_body", it) }
        intent.type = "vnd.android-dir/mms-sms"
        context.startActivity(intent)
    }

    fun checkAppInstalled(context: Context, pkgName: String): Boolean {
        if (pkgName.isEmpty()) {
            return false
        }
        var packageInfo: PackageInfo?
        try {
            packageInfo = context.packageManager.getPackageInfo(pkgName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            packageInfo = null
            e.printStackTrace()
        }
        return packageInfo != null
    }

    /**
     * 跳转到应用详情页面
     */
    private fun routeToAppDetail(context: Context) {
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.parse("package:${context.packageName}")
            context.startActivity(this)
        }
    }

}