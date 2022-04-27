# HoshiCore [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE_CN) [![LINK](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
Hoshi 纯净核心

## 使用说明
仅依赖最低限度的官方库，不引入其它第三方库，目前主要通过本地仓库引入或 AAR 引入
### 通过本地仓库引入
1. 打包上传到本地 Maven 仓库，如果仓库中已有所需的版本，跳过该步骤

   详见[发布步骤](#发布步骤)

2. setting.gradle 或相关配置内添加仓库引用
    ```
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
        repositories {
            ···
            mavenLocal() // 本地 Maven 仓库引用
        }
    }
    ```
3. 引入仓库并同步，仓库格式规则为：`${artifactGroup}:${artifactBuildId}:${artifactVersion}`
    ```
    implementation 'com.hoshi.lib:hoshi-armor:1.2.0'
    ```
### 通过 AAR 包来引入
1. 下载 AAR

   可以在 [releases](https://github.com/EndeRHoshI/HoshiArmor/releases) 下载，或者直接到本地仓库中取得，默认本地 Maven 仓库地址：C:\Users\userName\\.m2\repository\artifactGroup\artifactBuildId\artifactVersion

2. 将 AAR 放到 libs 文件夹下，build.gradle 中引入 AAR 并同步
    ```
    implementation(name: 'hoshi-armor-1.2.0', ext: 'aar')
    ```
## 发布步骤
1. 首先在 lib 中写好代码
2. 在 app 中写测试代码查看效果，然后分以下两种发布方案
    - 本地仓库
        1. 在 local-maven-armor.gradle 的 artifactVersion 中正确填写当前版本号
        2. 运行 publishToMavenLocal
        3. 项目中引用并测试
        4. 稳定后打个 tag，填写变更内容，同时保存好产物（这样可以远端引用依赖可以直接下载产物，使用 AAR 包来引入）
    - ~~Jitpack~~
        1. push 到自己的 github 库中
        2. 打 tag 和 release
        3. 去 [jitpack 官网](https://jitpack.io/) 对其进行打包
        4. 最后引用到相关的项目中