import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.san.busing"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.san.busing"
        minSdk = 31
        targetSdk = 34
        versionCode = 6
        versionName = "1.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", getApiKey("api.key"))
        buildConfigField("String", "ROUTE_PREFERENCE_KEY", getApiKey("route.preference.key"))
        buildConfigField("String", "STATION_PREFERENCE_KEY", getApiKey("station.preference.key"))
        buildConfigField("String", "STATION_URL", getApiKey("station.api.url"))
        buildConfigField("String", "LOCATION_URL", getApiKey("location.api.url"))
        buildConfigField("String", "ARRIVAL_URL", getApiKey("arrival.api.url"))
        buildConfigField("String", "ROUTES_URL", getApiKey("routes.api.url"))
        buildConfigField("String", "PRIVATE_INFO_TERM_URL", getApiKey("privateinfoterm.url"))
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

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

fun getApiKey(key: String): String {
    return gradleLocalProperties(rootDir).getProperty(key)
}

dependencies {
    implementation("androidx.activity:activity:1.9.0")
    val roomVersion = "2.6.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13")
    implementation("com.tickaroo.tikxml:annotation:0.8.13")
    implementation("com.tickaroo.tikxml:core:0.8.13")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.core:core-splashscreen:1.0.1")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    kapt("com.tickaroo.tikxml:processor:0.8.13")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    kapt("androidx.room:room-compiler:$roomVersion")
}