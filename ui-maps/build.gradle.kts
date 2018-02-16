val minSdk = properties["minSdk"].toString()
val kotlinVersion = properties["kotlinVersion"].toString()
val playServicesVersion = properties["playServicesVersion"].toString()

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(27)

    defaultConfig {
        minSdkVersion(minSdk)
    }
}

dependencies {
    api(project(":ui"))
    api("com.google.android.gms:play-services-maps:$playServicesVersion")

    implementation(kotlin("stdlib", kotlinVersion))
}