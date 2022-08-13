package com.hoshi.core.utils

import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import com.hoshi.core.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.text.DecimalFormat
import java.util.zip.CRC32
import java.util.zip.CheckedOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object FileUtils {

    const val BIT = 1L // 1
    const val BYTE = 8 * BIT // 8 bit
    const val KB = BYTE * 1024 // 8192 bit，1024 byte
    const val MB = KB * 1024
    const val GB = MB * 1024
    const val TB = GB * 1024

    /**
     * 只读模式
     */
    const val MODE_READ_ONLY = "r"

    fun isScopedStorageMode() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy()

    fun getFileByPath(filePath: String) = if (isSpace(filePath)) {
        null
    } else {
        File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        s ?: return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    fun isExists(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (file.exists()) {
            return true
        }

        return isExists(file.absolutePath)
    }

    fun isExists(filePath: String): Boolean {
        val file = getFileByPath(filePath) ?: return false
        if (file.exists()) {
            return true
        }
        return isFileExistsApi29(filePath)
    }

    /**
     * Android 10 判断文件是否存在的方法
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    private fun isFileExistsApi29(filePath: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            var afd: AssetFileDescriptor? = null
            try {
                val uri = Uri.parse(filePath)
                afd = openAssetFileDescriptor(uri)
                if (afd == null) {
                    return false
                } else {
                    closeIOQuietly(afd)
                }
            } catch (e: FileNotFoundException) {
                return false
            } finally {
                closeIOQuietly(afd)
            }
            return true
        }
        return false
    }

    /**
     * 安静关闭 IO
     *
     * @param closeables closeables
     */
    fun closeIOQuietly(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (ignored: IOException) {
                }
            }
        }
    }

    /**
     * 从uri资源符中读取文件描述
     *
     * @param uri 文本资源符
     * @return AssetFileDescriptor
     */
    fun openAssetFileDescriptor(
        uri: Uri
    ) = AppState.getApplicationContext().contentResolver.openAssetFileDescriptor(uri, MODE_READ_ONLY)

    /**
     * 压缩文件
     * @param fileList 要压缩的文件列表
     * @param targetPath 目标路径
     * @return 得到的产物
     */
    fun zipFile(fileList: List<File>, targetPath: String) {
        val targetFile = File(targetPath)
        try {
            ZipOutputStream(CheckedOutputStream(FileOutputStream(targetFile), CRC32())).use { zos ->
                fileList.forEach {
                    zipFile(zos, it)
                }
            }
        } catch (e: Exception) {
            HLog.e("FileUtils", e)
        }
    }

    private fun zipFile(zos: ZipOutputStream, file: File) {
        if (file.isDirectory) {
            file.listFiles()?.forEach { zipFile(zos, it) }
        } else {

            val size = 512 * KB.toInt()
            val byteArray = ByteArray(size)

            try {
                BufferedInputStream(FileInputStream(file), size).use { bis ->
                    zos.putNextEntry(ZipEntry(""))
                    var haha: Int
                    while (bis.read(byteArray).also { haha = it } != -1) {
                        zos.write(byteArray, 0, haha)
                    }
                }
                zos.flush()
                zos.closeEntry()
            } catch (e: FileNotFoundException) {
                HLog.e("FileUtils", e)
            } catch (e: IOException) {
                HLog.e("FileUtils", e)
            }
        }
    }

    /**
     * 删除文件夹下的文件
     * @param targetFile 目标文件
     * @param retainParent 保留文件夹
     */
    fun deleteFolder(targetFile: File, retainParent: Boolean = true) {
        if (targetFile.exists() && targetFile.isDirectory) {
            val files = targetFile.listFiles()
            files?.forEach {
                it.delete()
            }
            if (!retainParent) {
                targetFile.delete()
            }
        } else if (!retainParent) {
            targetFile.delete()
        }
    }

    /**
     * 删除文件夹下的文件
     * @param targetPath 目标文件路径
     * @param retainParent 保留文件夹
     */
    fun deleteFolder(targetPath: String, retainParent: Boolean = true) {
        val targetFile = File(targetPath)
        deleteFolder(targetFile, retainParent)
    }

    fun getFileList(folderPath: String): List<File> {
        val fileList = mutableListOf<File>()
        val folderFile = File(folderPath)
        return if (folderFile.exists() && folderFile.isDirectory) {
            val fileArray = folderFile.listFiles()
            fileArray?.sortByDescending { it?.lastModified() } // 按照修改时间排序
            fileArray?.forEach { file -> fileList.add(file) }
            fileList
        } else {
            fileList
        }
    }

    /**
     * 取得临时路径，该路径用于处理一些需要复制到沙盒内处理的场景
     * 注意该目录下文件在使用完后应该清除，或者每次打开应用都会清除，除非是生命周期极短的文件，不要放到这个目录下
     */
    @JvmStatic
    fun getTempDir() = getExtPriCacheDir("temp")

    /**
     * 获取外部存储私有目录下的 cache 目录，用户清空缓存时会清除此目录
     * @param folderName 子目录名，传空直接返回 cache 目录
     */
    fun getExtPriCacheDir(folderName: String? = null): String {
        val context = AppState.getApplicationContext()
        val dirBuilder = StringBuilder((context.externalCacheDir?.absolutePath ?: "") + "/")
        if (!folderName.isNullOrEmpty()) {
            dirBuilder.append("$folderName/")
        }
        val dir = dirBuilder.toString()
        createIfNoExists(dir)
        return dir
    }

    /**
     * 如果不存在的话，创建文件或文件夹
     * @param file String? 目标文件
     * @return Boolean 是否存在文件或文件夹
     */
    fun createIfNoExists(file: File?): Boolean {
        return if (file == null) {
            false
        } else {
            var exists = true
            if (!file.exists()) {
                exists = file.mkdirs()
            }
            exists
        }
    }

    /**
     * 如果不存在的话，创建文件或文件夹
     * @param filePath String? 目标路径
     * @return Boolean 是否存在文件或文件夹
     */
    fun createIfNoExists(filePath: String?): Boolean {
        return if (filePath.isNullOrEmpty()) {
            false
        } else {
            createIfNoExists(File(filePath))
        }
    }

    /**
     * 取得格式化文件大小
     * @param length Long 文件大小
     * @return String 格式化文件大小
     */
    fun getSizeFormat(length: Long): String {
        val df = DecimalFormat("######0.00")
        val l = length / 1000 // KB
        if (l < 1024) {
            return df.format(l) + "KB"
        } else if (l < 1024 * 1024F) {
            return df.format((l / 1024F)) + "MB"
        }
        return df.format(l / 1024F / 1024F) + "GB"
    }

    /**
     * 查询 StorageStats，以取得各种存储数据
     * TODO 看看如何兼容一下 O 以下的机型，以下是思路和要点
     * 1. Android8.0 之前没有对应的 API 提供，需要引入两个 AIDL 文件
     * 2. 引入文件后，使用反射进行相关接口的调用
     * 3. 反射调用时，是一个回调形式的方法，改成协程的同步写法来实现，以契合这个方法
     * 参考文章 [https://www.jianshu.com/p/d97c0de8bc18]
     * @return StorageStats?
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun queryStorageStat(): StorageStats? {
        val context = AppState.getApplicationContext()
        val uid = context.packageManager.getApplicationInfo(AppState.getPackageName(), PackageManager.GET_META_DATA).uid
        val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val uuid = StorageManager.UUID_DEFAULT
        val storageStats: StorageStats?
        withContext(Dispatchers.IO) {
            // 以下这两个效果一致
            // val cacheBytes = storageManager.getCacheSizeBytes(uuid)
            // val cacheBytes = stats.cacheBytes
            storageStats = runCatching { storageStatsManager.queryStatsForUid(uuid, uid) }.getOrNull() // 耗时操作
        }
        return storageStats
    }

    /**
     * 取得手机总容量和剩余容量
     * TODO 要看下 8.0 以下机型，没有 SD 卡时是怎样的，可以配合再深入看看存储相关的内容，研究下 SD 卡和手机存储的关系
     * @return Pair<Long, Long> first -> 总容量，second -> 剩余容量
     */
    fun queryPhoneTotalAndFree(): Pair<Long, Long> {
        val context = AppState.getApplicationContext()
        val totalBytes: Long
        val freeBytes: Long
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
            val uuid = StorageManager.UUID_DEFAULT
            totalBytes = storageStatsManager.getTotalBytes(uuid)
            freeBytes = storageStatsManager.getFreeBytes(uuid)
        } else {
            val statFs = StatFs(Environment.getExternalStorageDirectory().absolutePath + File.separator)
            totalBytes = statFs.totalBytes
            freeBytes = statFs.freeBytes
        }
        return Pair(totalBytes, freeBytes)
    }

    /**
     * 判断 SD Card 是否可用
     */
    fun isSDCardEnable() = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)

}

/**
 * 文件单位
 * @property step 当前级别，用于计算两个单位之间相差多少个级别
 */
sealed class Unit(val step: Int) {
    object Byte : Unit(0)
    object KB : Unit(1)
    object MB : Unit(2)
    object GB : Unit(3)
    object TB : Unit(4)
}