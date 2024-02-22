plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}
// apply("../local-maven.gradle") // 需要上传本地 Maven 仓库时才使用，否则不要解除注释

android {
    sourceSets.getByName("main") {
        java.srcDirs("src/main/kotlin")
    }

    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 33

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // 只在本模块依赖，因为打成 aar 之后不能传递，后续可以考虑连需要这些依赖的地方都移除掉
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2") // 协程

    implementation("androidx.startup:startup-runtime:1.1.1") // 统一处理初始化

    // jitpack 打包插件，最新版本可从 https://plugins.gradle.org/plugin/com.github.dcendents.android-maven 查看
    implementation("com.github.dcendents:android-maven-gradle-plugin:2.1")
}

// 需要这段代码才能推上仓库，可以看看具体是什么
project.afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["release"])
            }
        }
    }
}