object Versions {
    private const val compileAndTargetSdk = 35
    const val coreVersion = "1.0.3" // 小版本迭代是四段式，正式稳定版是三段式，比如 1.0.2.1 是改 bug 或者调试，用稳定后改成 1.0.3
    const val libName = "core" // 库名称
    const val compileSdk = compileAndTargetSdk
    const val targetSdk = compileAndTargetSdk
    const val minSdk = 24
}

object Strings {
    const val applicationId ="com.hoshi.core"
    const val androidXCore ="androidx.core:core:1.12.0"
    const val androidXCoreKtx ="androidx.core:core-ktx:1.12.0"
    const val androidXAppCompat ="androidx.appcompat:appcompat:1.6.1"
}