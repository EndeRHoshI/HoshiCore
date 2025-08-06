package com.hoshi.core.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresPermission
import com.hoshi.core.AppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

/**
 * 网络工具类，部分方法参考自：https://juejin.cn/post/7098947365976932383
 * Created by lv.qx on 2024/4/17
 */
object NetUtils {

    private const val interfaceName = "eth0"

    /**
     * 获取当前网络的 ip 地址
     * @return String
     */
    private fun getIp(): String {
        NetworkInterface.getNetworkInterfaces().let {
            loo@ for (networkInterface in Collections.list(it)) {
                for (inetAddresses in Collections.list(networkInterface.inetAddresses)) {
                    if (inetAddresses is Inet4Address && !inetAddresses.isLoopbackAddress && !inetAddresses.isLinkLocalAddress) {
                        return inetAddresses.hostAddress ?: "0.0.0.0"
                    }
                }
            }
        }
        return "没有 ip"
    }

    /**
     * @return String 取得当前网关
     */
    fun getGateWay(): String {
        runCatching {
            val process = Runtime.getRuntime().exec("ip route list table 0")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            val string = bufferedReader.readLine()
            return string.split("\\s+").last()
        }.onFailure { HLog.e(it) }
        return "0.0.0.0"
    }

    /**
     * @return String 取得子网掩码
     */
    fun getSubnetMask(): String {
        runCatching {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces() // 获取本机所有的网络接口
            while (networkInterfaces.hasMoreElements()) {  // 判断 Enumeration 对象中是否还有数据
                val networkInterface = networkInterfaces.nextElement()   // 获取 Enumeration 对象中的下一个数据
                if (!networkInterface.isUp) { // 判断网口是否在使用
                    continue
                }
                if (interfaceName != networkInterface.displayName) { // 网口名称是否和需要的相同
                    continue
                }
                networkInterface.interfaceAddresses.forEach { interfaceAddress ->
                    if (interfaceAddress.address is Inet4Address) { // 仅仅处理 ipv4
                        // 获取掩码位数，通过 calcMaskByPrefixLength 转换为字符串
                        return calcMaskByPrefixLength(interfaceAddress.networkPrefixLength);
                    }
                }
            }
        }.onFailure { HLog.e(it) }

        return "0.0.0.0"
    }

    /**
     * 通过子网掩码的位数计算子网掩码
     * @param length Int 子网掩码的位数
     * @return String 子网掩码
     */
    private fun calcMaskByPrefixLength(length: Short): String {
        val mask = -0x1 shl 32 - length
        val partsNum = 4
        val bitsOfPart = 8
        val maskParts = IntArray(partsNum)
        val selector = 0x000000ff

        for (i in maskParts.indices) {
            val pos = maskParts.size - 1 - i
            maskParts[pos] = mask shr i * bitsOfPart and selector
        }

        var result = ""
        result += maskParts[0]

        for (i in 1 until maskParts.size) {
            result = result + "." + maskParts[i]
        }
        return result
    }

    /**
     * @return Boolean 是否连上 WiFi
     */
    fun isWiFiConnected(): Boolean {
        val wifiMgr = AppState.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiMgr.isWifiEnabled
    }

    /**
     * @return Boolean 是否连上网络
     */
    @SuppressLint("ObsoleteSdkInt")
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkConnected(): Boolean {
        val connectivityManager = AppState.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            network != null //如果是 null 代表没有网络
        } else {
            val network = connectivityManager.activeNetworkInfo
            network != null //如果是 null 代表没有网络
        }
    }

}