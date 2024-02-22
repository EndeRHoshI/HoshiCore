// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "1.6.21"
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.41") // Hilt 的插件路径
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.0")
        // classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.23.4") Jfrog 相关，暂时无用

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module les
    }
}