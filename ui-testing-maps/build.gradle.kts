val kotlinVersion: String by project
val playServicesVersion: String by project

plugins {
    id("com.android.library")
    kotlin("android")
}

dependencies {
    api(project(":ui-testing-core"))
    api("com.google.android.gms:play-services-maps:$playServicesVersion")

    implementation(kotlin("stdlib", kotlinVersion))
}