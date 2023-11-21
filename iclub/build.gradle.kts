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

    //关于glide的引用
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation(files("libs\\mysql-connector-java-5.1.46.jar"))

    implementation("androidx.core:core-ktx:1.7.0")



    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //引用此依赖后没有出现Debug相关的报错
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    //时间选择器
    implementation("com.github.loper7:DateTimePicker:0.6.3")

}