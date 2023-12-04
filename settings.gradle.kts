pluginManagement {
    repositories {
//        google()
//        mavenCentral()
//        gradlePluginPortal()

        maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
        maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
        google()

    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }



}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
//        google()
//        mavenCentral()
//        maven {
//            url = uri("https://www.jitpack.io")
//        }

        maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
        maven { url 'http://maven.aliyun.com/nexus/content/repositories/jcenter' }
        google()

    }
}

rootProject.name = "Test Application 01"
include(":app")
include(":chapter04")
include(":iclub")
