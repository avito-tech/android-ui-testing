val targetSdk: String by project
val minSdk: String by project

val kotlinVersion: String by project
val playServicesVersion: String by project
val appcompatVersion: String by project
val recyclerViewVersion: String by project
val materialVersion: String by project

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

    buildTypes {
        getByName("debug") {
            matchingFallbacks = listOf("release")
        }
    }

    variantFilter {
        if (name == "release") {
            setIgnore(true)
        }
    }

    packagingOptions {
        pickFirst("protobuf.meta")
    }
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation("com.google.android.gms:play-services-maps:$playServicesVersion")

    api("androidx.appcompat:appcompat:$appcompatVersion")
    api("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    api("com.google.android.material:material:$materialVersion")

    androidTestImplementation(project(":ui-testing-core"))
}

tasks.getByName("build").dependsOn("$path:assembleAndroidTest")
