import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import com.jfrog.bintray.gradle.BintrayExtension.VersionConfig
import org.gradle.api.publish.maven.MavenPom

val kotlinVersion: String by project
val espressoVersion: String by project
val supportVersion: String by project
val junitVersion: String by project

plugins {
    id("com.android.library")
    kotlin("android")
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