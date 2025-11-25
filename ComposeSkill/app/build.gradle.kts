import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    alias(libs.plugins.kotlin.parcelize)  // ✅ 이제 alias로 호출 가능
    alias(libs.plugins.hilt)          // ✅ hilt alias 사용
}

val localProps = project.rootProject.file("local.properties")
val properties = Properties().apply {
    if (localProps.exists()) {
        load(localProps.inputStream())
    }
}

val API_Client: String = properties.getProperty("API_Client") as String? ?: ""
val API_Secret: String = properties.getProperty("API_Secret") as String? ?: ""
val BASE_URL: String = properties.getProperty("BASE_URL") as String? ?: ""


android {
    namespace = "com.sonici16.composeskill"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sonici16.composeskill"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_Client", "\"$API_Client\"")
        buildConfigField("String", "API_Secret", "\"$API_Secret\"")
        buildConfigField("String", "BASE_URL", "\"$BASE_URL\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // build.gradle(app)
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.compose.foundation)
    annotationProcessor(libs.hilt.compiler)
    kapt("com.google.dagger:hilt-compiler:2.52")  // ✅ kapt 의존성
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    implementation("io.coil-kt:coil-compose:2.4.0")
    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.squareup.retrofit2:adapter-rxjava2:2.2.0")
    implementation("com.squareup.okhttp3:okhttp:3.6.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.6.0")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2") // CoroutineCallAdapterFactory 추가
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9") // coroutine
    implementation ("com.google.accompanist:accompanist-pager:0.28.0")

    implementation ("io.reactivex.rxjava2:rxandroid:2.1.0")
    implementation ("io.reactivex.rxjava2:rxjava:2.0.0")
    implementation ("io.reactivex.rxjava2:rxandroid:2.0.0")


    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}