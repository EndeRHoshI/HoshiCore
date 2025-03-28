pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // 添加阿里云 maven 地址
        maven("https://maven.aliyun.com/nexus/content/groups/public")
        maven("https://jitpack.io")

        mavenLocal()
    }
}

rootProject.name = "HoshiCore"
include(":app")
include(":core")