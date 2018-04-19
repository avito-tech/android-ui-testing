val kotlinVersion: String by project
val playServicesVersion: String by project
val targetSdk: String by project
val minSdk: String by project
val supportVersion: String by project

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
    implementation(kotlin("stdlib", kotlinVersion))
    implementation("com.google.android.gms:play-services-maps:$playServicesVersion")
    implementation("com.android.support:appcompat-v7:$supportVersion")
    implementation("com.android.support:recyclerview-v7:$supportVersion")
    implementation("com.android.support:design:$supportVersion")

    androidTestImplementation(project(":ui-testing-core"))
}