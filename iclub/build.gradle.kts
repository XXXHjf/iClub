import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.icluub"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.icluub"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}



dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("io.github.youth5201314:banner:2.2.2")  // banner依赖(成功)

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

    implementation("androidx.core:core-ktx:1.7.0")

    // 阿里云OSS的依赖项
    implementation("com.aliyun.dpa:oss-android-sdk:2.9.13")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // 引用此依赖后没有出现Debug相关的报错
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // 时间选择器
    implementation("com.github.loper7:DateTimePicker:0.6.3")

}