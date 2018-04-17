val kotlinVersion: String by project
val playServicesVersion: String by project
val targetSdk: String by project
val minSdk: String by project

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(targetSdk.toInt())

    defaultConfig {
        minSdkVersion(minSdk)
        targetSdkVersion(targetSdk.toInt())
        testInstrumentationRunner = "com.avito.android.ui.test.UITestRunner"
    }

    packagingOptions {
        pickFirst("protobuf.meta")
    }
}

dependencies {
    api(project(":ui-testing-core"))
    api("com.google.android.gms:play-services-maps:$playServicesVersion")

    implementation(kotlin("stdlib", kotlinVersion))

    androidTestImplementation(project(":ui-testing-core"))
}