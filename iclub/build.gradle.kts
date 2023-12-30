import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.icluub"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.icluub"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }
    packagingOptions {
        exclude("META-INF/kotlinx_coroutines_core.version")
        exclude("META-INF/DEPENDENCIES")
        exclude("mozilla/public-suffix-list.txt")
        exclude("project.properties")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/NOTICE.md")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}




dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // banner轮播图库
    implementation ("io.github.youth5201314:banner:2.2.2")

    // glide图片加载框架
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    // JDBC本地库
    implementation(files("libs\\mysql-connector-java-5.1.46.jar"))

    // 权限请求框架
    implementation("com.github.tbruyelle:rxpermissions:0.12")

    // 异步操作操作框架（rxandroid是针对rxjava在android平台的优化）
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    implementation("androidx.core:core-ktx:1.9.0")

    // 阿里云OSS的依赖项
    implementation("com.aliyun.dpa:oss-android-sdk:2.9.18")
    // 阿里云core核心库
    implementation("com.aliyun:aliyun-java-sdk-core:4.6.4")
    implementation("com.aliyun:aliyun-java-sdk-sts:3.1.2")
    // 引入STS SDK
    // https://mvnrepository.com/artifact/com.aliyun/aliyun-java-sdk-sts
    implementation("com.aliyun:alibabacloud-sts20150401:1.0.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // recyclerview库
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    // 时间选择器
    implementation("com.github.loper7:DateTimePicker:0.6.3")
    // 引入华为统一扫码服务（Scan Kit）
    implementation("com.huawei.hms:scan:2.12.0.301")

//    // OkHttp的核心库
//    implementation("com.squareup.okhttp3:okhttp:4.12.0")
//    // OkHttpUtils库
//    implementation("com.zhy:okhttputils:2.6.2")
//    implementation("com.github.xuexiangjys.XUtil:xutil-core:2.0.01")
//
//    // 引入XUpdate更新框架
//    implementation("com.github.xuexiangjys:XUpdate:2.1.4")
}