pluginManagement {
    repositories {
        maven { url "https://jitpack.io" }
        maven { url 'https://maven.aliyun.com/repository/releases' }
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
        maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
        maven { url 'https://maven.aliyun.com/repository/public' }
        maven {url 'https://developer.huawei.com/repo/'}
        google()
        mavenCentral()
        gradlePluginPortal()

    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.0'
        classpath 'com.huawei.agconnect:agcp:1.6.0.300

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }



}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url "https://jitpack.io" }
        maven { url 'https://maven.aliyun.com/repository/releases' }
        maven { url 'https://maven.aliyun.com/repository/google' }
        maven { url 'https://maven.aliyun.com/repository/central' }
        maven { url 'https://maven.aliyun.com/repository/gradle-plugin' }
        maven { url 'https://maven.aliyun.com/repository/public' }
        maven {url 'https://developer.huawei.com/repo/'}
        google()
        mavenCentral()
        gradlePluginPortal()

    }
}

rootProject.name = "Software Project iClub"
include(":app")
include(":chapter04")
include(":iclub")
