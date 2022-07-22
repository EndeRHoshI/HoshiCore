package com.hoshi.core.utils

import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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

    fun isExists(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty()) {
            return false
        }

        return File(filePath).exists()
    }

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
            // 这两个效果一致
            // val cacheBytes = storageManager.getCacheSizeBytes(uuid)
            // val cacheBytes = stats.cacheBytes
            storageStats = runCatching { storageStatsManager.queryStatsForUid(uuid, uid) }.getOrNull() // 耗时操作
        }
        return storageStats
    }

    /**
     * 取得手机总容量和剩余容量
     * @return Pair<Long, Long> first -> 总容量，second -> 剩余容量
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun queryPhoneTotalAndFree(): Pair<Long, Long> {
        val context = AppState.getApplicationContext()
        val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val uuid = StorageManager.UUID_DEFAULT
        val totalBytes = storageStatsManager.getTotalBytes(uuid)
        val freeBytes = storageStatsManager.getFreeBytes(uuid)
        return Pair(totalBytes, freeBytes)
    }

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