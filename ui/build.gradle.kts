val minSdk = properties["minSdk"].toString()
val kotlinVersion = properties["kotlinVersion"].toString()
val espressoVersion = properties["espressoVersion"].toString()
val supportVersion = properties["supportVersion"].toString()
val junitVersion = properties["junitVersion"].toString()

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
    api("com.android.support.test.espresso:espresso-core:$espressoVersion")
    api("com.android.support.test.espresso:espresso-contrib:$espressoVersion")
    api("com.android.support.test.espresso:espresso-intents:$espressoVersion")
    api("com.android.support.test.uiautomator:uiautomator-v18:2.1.3")
    api("com.forkingcode.espresso.contrib:espresso-descendant-actions:1.2.0")
    api("com.android.support:appcompat-v7:$supportVersion")
    api("com.android.support:recyclerview-v7:$supportVersion")

    implementation(kotlin("stdlib", kotlinVersion))
    implementation("com.google.code.gson:gson:2.8.2")
    implementation("org.hamcrest:hamcrest-library:1.3")
    implementation("junit:junit:$junitVersion")
}