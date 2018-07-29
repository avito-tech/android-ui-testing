plugins {
    id("com.android.library")
    kotlin("android")
}

val kotlinVersion: String by project
val junitVersion: String by project
val targetSdk: String by project
val minSdk: String by project

android {
    compileSdkVersion(targetSdk.toInt())

    defaultConfig {
        minSdkVersion(minSdk)
        targetSdkVersion(targetSdk.toInt())
    }
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation("com.squareup.okhttp3:okhttp:3.11.0")

    testImplementation("junit:junit:$junitVersion")
}