package com.hoshi.core.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

/**
 * 加解密工具类
 * Created by lv.qx on 2022/5/16
 */
object EncryptUtils {

    private const val AES = "AES"
    private const val DES = "DES"

    fun encryptAES(plaintext: String, key: String = ""): String {
        val keyGenerator = KeyGenerator.getInstance(AES) // 创建 AES 的 key 生产者

        // 利用用户密码作为随机数初始化出 128 位的 key 生产者，SecureRandom 是生产安全随机数序列，password.getBytes() 是种子
        // 只要种子相同，序列就一样，所以解密只要有 password 就行
        keyGenerator.init(128, SecureRandom(key.toByteArray()))

        val secretKey = keyGenerator.generateKey() // 根据用户密码生成一个密钥
        val keyByteArray = secretKey.encoded // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null
        val plaintextByteArray = plaintext.toByteArray()
        val secretKeySpec = SecretKeySpec(keyByteArray, AES) // 转换为 AES 专用密钥
        val cipher = Cipher.getInstance(AES) // 创建密码器
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec) // 初始化为解密模式的密码器
        val resultByte = cipher.doFinal(plaintextByteArray)
        return Base64.encodeToString(resultByte, Base64.NO_WRAP)
    }

    fun decryptAES(ciphertext: String, key: String = ""): String {
        val keyGenerator = KeyGenerator.getInstance(AES) // 创建 AES 的 key 生产者

        // 利用用户密码作为随机数初始化出 128 位的 key 生产者，SecureRandom 是生产安全随机数序列，password.getBytes() 是种子
        // 只要种子相同，序列就一样，所以解密只要有 password 就行
        keyGenerator.init(128, SecureRandom(key.toByteArray()))

        val secretKey = keyGenerator.generateKey() // 根据用户密码生成一个密钥
        val keyByteArray = secretKey.encoded // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null
        val ciphertextByteArray = ciphertext.toByteArray()
        val secretKeySpec = SecretKeySpec(keyByteArray, AES) // 转换为 AES 专用密钥
        val cipher = Cipher.getInstance(AES) // 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec) // 初始化为解密模式的密码器
        val resultByte = cipher.doFinal(ciphertextByteArray)
        return Base64.encodeToString(resultByte, Base64.NO_WRAP)
    }

    fun encryptDES(plaintext: String, key: String = ""): String {
        val cipher = Cipher.getInstance(DES) // 创建密码器
        val keyByteArray = key.toByteArray()
        val plaintextByteArray = plaintext.toByteArray()
        val secretKeySpec = SecretKeySpec(keyByteArray, DES)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
        return cipher.doFinal(plaintextByteArray).toString()
    }

    fun decryptDES(ciphertext: String, key: String = ""): String {
        val cipher = Cipher.getInstance(DES) // 创建密码器
        val keyByteArray = key.toByteArray()
        val plaintextByteArray = ciphertext.toByteArray()
        val secretKeySpec = SecretKeySpec(keyByteArray, DES)
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
        return cipher.doFinal(plaintextByteArray).toString()
    }

}