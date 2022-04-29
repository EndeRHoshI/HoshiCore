plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}
apply("../local-maven.gradle")

android {
    sourceSets.getByName("main") {
        java.srcDirs("src/main/kotlin")
    }

    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk  = 32

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
    // 纯净核心，依赖创建 No Activity Project 时所需的库
    api("androidx.core:core-ktx:1.7.0")
    api("androidx.appcompat:appcompat:1.4.1")
    api("com.google.android.material:material:1.5.0")

    // 另加 ConstraintLayout 和协程
    api("androidx.constraintlayout:constraintlayout:2.1.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
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