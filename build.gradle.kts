import com.android.build.api.dsl.extension.AndroidExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import com.android.builder.model.AndroidLibrary
import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.publish.maven.MavenPom
import kotlin.properties.Delegates

buildscript {
    val kotlinVersion: String by project

    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath("com.android.tools.build:gradle:3.1.3")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}

group = "com.avito.ui-testing"
version = "0.2.1"

val minSdk: String by project
val targetSdk: String by project

subprojects {
    repositories {
        jcenter()
        google()
    }

    group = rootProject.group
    version = rootProject.version

    plugins.withType(LibraryPlugin::class.java) {

        apply(plugin = "com.github.dcendents.android-maven")
        apply<BintrayPlugin>()

        val sourcesJarTask = tasks.create<Jar>("sourcesJar") {
            classifier = "sources"
            from(
                this@subprojects.extensions.getByType(BaseExtension::class.java).sourceSets.getByName(
                    "main"
                ).java.srcDirs
            )
        }

        val installTask = tasks.getByName("install")

        (installTask as Upload).run {
            DslObject(repositories).convention.getPlugin(MavenRepositoryHandlerConvention::class.java)
                .mavenInstaller {
                    pom {
                        project {
                            packaging = "aar"
                            groupId = rootProject.group.toString()
                            artifactId = name
                            version = rootProject.version.toString()
                        }
                    }
                }
        }

        tasks.getByName("bintrayUpload").dependsOn(installTask)

        artifacts {
            add("archives", sourcesJarTask)
        }

        configure<BintrayExtension> {
            user = System.getenv("BINTRAY_USER")
            key = System.getenv("BINTRAY_API_KEY")

            publish = true
            setConfigurations("archives")

            pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
                repo = "maven"
                name = "android-ui-testing"
                userOrg = "avito-tech"
                vcsUrl = "https://github.com/avito-tech/android-ui-testing"
                setLabels("kotlin")
                setLicenses("MIT")

                version(delegateClosureOf<BintrayExtension.VersionConfig> {
                    name = rootProject.version.toString()

                    gpg(delegateClosureOf<BintrayExtension.GpgConfig> {
                        sign = true
                        passphrase = System.getenv("BINTRAY_GPG_PASSPHRASE")
                    })
                })
            })
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "4.9"
    distributionType = Wrapper.DistributionType.BIN
}

task<Delete>("clean") { delete("${rootProject.buildDir}") }