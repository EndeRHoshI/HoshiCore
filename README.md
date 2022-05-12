# HoshiCore [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE_CN) [![LINK](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
Hoshi 纯净核心，仅依赖最低限度的官方库，不引入其它第三方库

## 使用说明
目前主要通过本地仓库引入或 AAR 引入
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
    implementation 'com.hoshi.lib:hoshi-core:0.0.1'
    ```
### 通过 AAR 包来引入
1. 下载 AAR

   可以在 [releases](https://github.com/EndeRHoshI/HoshiCore/releases) 下载，或者直接到本地仓库中取得，默认本地 Maven 仓库地址：C:\Users\userName\\.m2\repository\artifactGroup\artifactBuildId\artifactVersion

2. 将 AAR 放到 app/src 同级的 libs 文件夹下，build.gradle 中引入 AAR 并同步
    ```
    implementation(files("./libs/hoshi-core-0.0.1.aar"))
    ```
## 发布步骤
1. 首先在 lib 中写好代码
2. 在 app 中写测试代码查看效果
3. 在 local-maven.gradle 的 artifactVersion 中正确填写当前版本号
4. 运行 publishToMavenLocal
5. 目标项目中引用并测试
6. 稳定后打个 tag，填写变更内容，同时保存好产物（这样远端引用依赖时，可以脱离本地 Maven，直接下载产物，使用 AAR 包来引入）

注意：尽量采用 Maven 仓库的方式发布并依赖，除非项目足够简单只需要引用 HoshiCore，否则单纯使用 AAR 包的话，把 AAR 再打进另一个库（比如 HoshiArmor）并不是简单的事情