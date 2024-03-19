plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.yw1573.tred"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yw1573.tred"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "v1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    dataBinding {
        enable = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    /////////////////////////////////////////////////////////////////////////////////////////
    // 日期时间控件
    implementation("com.github.loper7:DateTimePicker:0.6.3")
    // 对话框核心库
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    // 对话框扩展-文本输入对话框
    implementation("com.afollestad.material-dialogs:input:3.3.0")
    // 折线图库
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // 版本更新库
    implementation("io.github.azhon:appupdate:4.3.2")
    // BRVAH
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.4")
}