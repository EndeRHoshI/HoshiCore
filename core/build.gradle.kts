plugins {
    id("com.android.library")
    id("kotlin-android")
    `maven-publish`
}

@Suppress("UnstableApiUsage") // 去掉一些不稳定 Api 的警告
android {
    namespace = "com.hoshi.core" // 新版本 AGP 整出来的东西，新建项目会自带
    sourceSets.getByName("main") {
        java.srcDirs("src/main/kotlin")
    }

    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // 添加版本字段到 BuildConfig
            buildConfigField("String", "CORE_VERSION", "\"${Versions.coreVersion}\"")
        }
        debug {
            // 添加版本字段到 BuildConfig
            buildConfigField("String", "CORE_VERSION", "\"${Versions.coreVersion}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true // 启用 BuildConfig 功能
    }
}

dependencies {
    // 只在本模块依赖，因为打成 aar 之后不能传递，后续可以考虑连需要这些依赖的地方都移除掉
    implementation(Strings.androidXCore)
    implementation(Strings.androidXCoreKtx)
    implementation(Strings.androidXAppCompat)
    implementation("com.google.android.material:material:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1") // 协程

    implementation("androidx.startup:startup-runtime:1.1.1") // 统一处理初始化

    // jitpack 打包插件，最新版本可从 https://plugins.gradle.org/plugin/com.github.dcendents.android-maven 查看
    implementation("com.github.dcendents:android-maven-gradle-plugin:2.1")
}

// 需要这段代码才能推上仓库，可以看看具体是什么
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.hoshi.lib"
            artifactId = "hoshi-${Versions.libName}"
            version = Versions.coreVersion

            afterEvaluate {
                from(components["release"])
            }
        }
        repositories {
            maven {
                name = Versions.libName
                url = uri(layout.buildDirectory.dir(Versions.libName))
            }
        }
    }
}