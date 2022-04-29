plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    sourceSets.getByName("main") {
        java.srcDirs("src/main/kotlin")
    }

    compileSdk = 32

    defaultConfig {
        applicationId = "com.hoshi.core"
        minSdk = 23
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // getDefaultProguardFile() 方法是获取 ANDROID_SDK\tools\proguard 目录下的文件
            // 那么上述命令就是获取 ANDROID_SDK\tools\proguard\proguard-android.txt 这个文件
            // 如果你想进一步压缩代码就可以改使用 getDefaultProguardFile("proguard-android-optimize.txt") ，但是会更加耗时
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding.isEnabled = true
}

dependencies {
    implementation(project(":core")) // 依赖 Hoshi 核心

    val ktxVersion = "2.3.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$ktxVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$ktxVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$ktxVersion")
}