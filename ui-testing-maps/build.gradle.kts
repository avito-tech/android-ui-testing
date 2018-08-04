val kotlinVersion: String by project
val playServicesVersion: String by project
val targetSdk: String by project
val minSdk: String by project

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(targetSdk.toInt())

    defaultConfig {
        minSdkVersion(minSdk)
        targetSdkVersion(targetSdk.toInt())
    }
}

dependencies {
    api(project(":ui-testing-core"))
    api("com.google.android.gms:play-services-maps:$playServicesVersion")

    implementation(kotlin("stdlib", kotlinVersion))
}
