import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.apache.tools.ant.property.LocalProperties
import org.bouncycastle.util.Properties
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.san.busing"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.san.busing"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", getApiKey("api.key"))
        buildConfigField("String", "STATION_URL", getApiKey("station.api.url"))
        buildConfigField("String", "LOCATION_URL", getApiKey("location.api.url"))
        buildConfigField("String", "ARRIVAL_URL", getApiKey("arrival.api.url"))
        buildConfigField("String", "ROUTES_URL", getApiKey("routes.api.url"))
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}