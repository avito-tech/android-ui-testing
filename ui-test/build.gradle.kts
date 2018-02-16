val minSdk = properties["minSdk"].toString()
val kotlinVersion = properties["kotlinVersion"].toString()
val playServicesVersion = properties["playServicesVersion"].toString()

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(27)

    defaultConfig {
        minSdkVersion(minSdk)
        testInstrumentationRunner = "com.avito.android.ui.test.UITestRunner"
    }

    packagingOptions {
        pickFirst("protobuf.meta")
    }
}


dependencies {
    api(project(":ui"))
    api("com.google.android.gms:play-services-maps:$playServicesVersion")

    implementation(kotlin("stdlib", kotlinVersion))

    androidTestImplementation(project(":ui"))
}