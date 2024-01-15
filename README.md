# HoshiCore [![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE_CN) [![LINK](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
Hoshi 纯净核心，仅依赖最低限度的官方库，不引入其它第三方库

## 使用说明
目前主要通过 Jitpack、本地 Maven 仓库或 AAR 引入
### 通过 Jitpack 引入
1. 更新代码后，提升 artifactVersion
2. 推到远端 git 仓库
3. 进入 [Jitpack 官网](https://jitpack.io/)，搜索 `EndeRHoshI/HoshiCore`
4. 点击 Get it，跟随 How to 中说明的来做，大概如下：
   1. Add the JitPack repository to your build file
      ```
      dependencyResolutionManagement {
          ositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
          repositories {
               mavenCentral()
               maven { url 'https://jitpack.io' }
          }
      }  
      ```
   2. Add the dependency
      ```
      dependencies {
          implementation 'com.github.EndeRHoshI:HoshiCore:Tag'
      }
      ```
      其中的 Tag 表示当前版本：[![](https://jitpack.io/v/EndeRHoshI/HoshiCore.svg)](https://jitpack.io/#EndeRHoshI/HoshiCore)
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
    implementation 'com.hoshi.lib:hoshi-core:0.0.3'
    ```
### 通过 AAR 包来引入
1. 下载 AAR

   可以在 [releases](https://github.com/EndeRHoshI/HoshiCore/releases) 下载，或者直接到本地仓库中取得，默认本地 Maven 仓库地址：C:\Users\userName\\.m2\repository\artifactGroup\artifactBuildId\artifactVersion

2. 将 AAR 放到 app/src 同级的 libs 文件夹下，build.gradle 中引入 AAR 并同步
    ```
    implementation(files("./libs/hoshi-core-0.0.3.aar"))
    ```
   如果不使用 KTS，则是如下代码即可
    ```
    implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
    ```
## 发布步骤
1. 首先在 core 模块中写好代码
2. 在 app 中写测试代码查看效果
3. 在 local-maven.gradle 的 artifactVersion 中正确填写当前版本号
4. 运行 Gradle 快捷指令列表中的 publishing 中的 publishToMavenLocal
5. 目标项目中引用并测试
6. 稳定后可以进行打 tag，在 AS 中的 Git 记录中右键添加 Tag
7. 添加完后用指令 `git push origin <tagName>` 把 Tag 推到远端仓库
8. Github 上面创建 Release，指向刚刚的 Tag，并填写变更内容，同时上传产物（这样远端引用依赖时，可以脱离本地 Maven，直接下载产物，使用 AAR 包来引入）

注意：尽量采用 Maven 仓库的方式发布并依赖，除非项目足够简单只需要引用 HoshiCore，否则单纯使用 AAR 包的话，把 AAR 再打进另一个库（比如 HoshiArmor）并不是简单的事情