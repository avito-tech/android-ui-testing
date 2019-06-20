import com.android.build.gradle.LibraryPlugin
import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import groovy.lang.GroovyObject
import org.gradle.api.internal.plugins.DslObject
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig

plugins {
    `kotlin-dsl` apply false
    id("com.android.application") apply false
    id("com.github.dcendents.android-maven") version "2.1" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
    id("com.jfrog.artifactory") version "4.7.5" apply false
}

group = "com.avito.ui-testing"
version = "0.4.4-snapshot"

val minSdk: String by project
val targetSdk: String by project
val androidStudioPath: String? by project
val supportVersion: String by project

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force("com.android.support:support-v4:$supportVersion")
            force("com.android.support:appcompat-v7:$supportVersion")
            force("com.android.support:recyclerview-v7:$supportVersion")
            force("com.android.support:design:$supportVersion")
        }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.withType<LibraryPlugin> {

        apply(plugin = "com.github.dcendents.android-maven")
        apply<BintrayPlugin>()
        apply<ArtifactoryPlugin>()

        // we don't need debug variant for libraries at all
        extension.variantFilter {
            if (name == "debug") setIgnore(true)
        }

        val sourcesJarTask = tasks.create<Jar>("sourcesJar") {
            classifier = "sources"
            from(this@withType.extension.sourceSets["main"].java.srcDirs)
        }

        (tasks["install"] as Upload).run {
            DslObject(repositories).convention
                .getPlugin<MavenRepositoryHandlerConvention>()
                .mavenInstaller {
                    pom {
                        project {
                            packaging = "aar"
                            groupId = rootProject.group.toString()
                            artifactId = this@subprojects.name
                            version = rootProject.version.toString()
                        }
                    }
                }

            tasks["bintrayUpload"].dependsOn(this)
            tasks["artifactoryPublish"].dependsOn(this)
        }

        val configurationName = "archives"

        artifacts {
            add(configurationName, sourcesJarTask)
        }

        // Release artifacts uploading
        configure<BintrayExtension> {
            user = System.getenv("BINTRAY_USER")
            key = System.getenv("BINTRAY_API_KEY")

            publish = true
            setConfigurations(configurationName)

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

        // Snapshot artifacts uploading
        configure<ArtifactoryPluginConvention> {
            setContextUrl(System.getenv("ARTIFACTORY_URL"))
            publish(delegateClosureOf<PublisherConfig> {
                repository(delegateClosureOf<GroovyObject> {
                    setProperty("repoKey", System.getenv("ARTIFACTORY_REPO"))
                    setProperty("username", System.getenv("ARTIFACTORY_USER"))
                    setProperty("password", System.getenv("ARTIFACTORY_PASSWORD"))
                    setProperty("maven", true)
                })
                defaults(delegateClosureOf<GroovyObject> {
                    invokeMethod("publishConfigs", configurationName)
                })
            })
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "5.2.1"
    distributionType = Wrapper.DistributionType.BIN
}

val checkTask = task("check") {
    group = "verification"
}

task("build") {
    group = "build"
    dependsOn(checkTask)
}

task<Delete>("clean") {
    group = "build"
    delete("${rootProject.buildDir}")
}
