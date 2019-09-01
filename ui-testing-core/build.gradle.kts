val targetSdk: String by project
val minSdk: String by project

val androidXTestVersion: String by project
val uiAutomatorVersion: String by project
val recyclerViewVersion: String by project
val materialVersion: String by project
val appcompatVersion: String by project
val espressoVersion: String by project
val junitVersion: String by project
val kotlinVersion: String by project

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
    api("androidx.test:core:$androidXTestVersion")
    api("androidx.test.espresso:espresso-core:$espressoVersion")
    api("androidx.test.espresso:espresso-web:$espressoVersion")
    api("androidx.test.espresso:espresso-intents:$espressoVersion")
    api("androidx.test.uiautomator:uiautomator:$uiAutomatorVersion")

    api("com.forkingcode.espresso.contrib:espresso-descendant-actions:1.4.0")

    api("androidx.appcompat:appcompat:$appcompatVersion")
    api("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    api("com.google.android.material:material:$materialVersion")

    implementation(kotlin("stdlib", kotlinVersion))
    implementation("org.hamcrest:hamcrest-library:1.3")
    implementation("junit:junit:$junitVersion")
}

tasks.withType<Test> {
    testLogging {
        events("passed", "skipped", "failed")
    }
}
