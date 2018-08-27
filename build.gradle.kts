import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import com.android.ide.common.xml.XmlFormatPreferences.defaults
import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import groovy.lang.GroovyObject
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.internal.plugins.DslObject
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.dsl.DoubleDelegateWrapper
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

plugins {
    `kotlin-dsl` apply false
    id("com.android.application") apply false
    id("com.github.dcendents.android-maven") version "2.1" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
    id("com.jfrog.artifactory") version "4.7.5" apply false
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC8"
}

group = "com.avito.ui-testing"
version = "0.2.5-SNAPSHOT"

val minSdk: String by project
val targetSdk: String by project
val androidStudioPath: String? by project

detekt {
    version = "1.0.0.RC8"
    defaultProfile(Action {
        input = rootProject.projectDir.absolutePath
        config = "$rootDir/detekt-config.yml"
        filters = ".*/resources/.*,.*/build/.*"
    })
}

dependencies {
    detekt("io.gitlab.arturbosch.detekt:detekt-formatting:${detekt.version}")
}

allprojects {
    repositories {
        jcenter()
        google()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    plugins.withType(LibraryPlugin::class.java) {

        apply(plugin = "com.github.dcendents.android-maven")

        // we don't need debug variant for libraries at all
        configure<BaseExtension> {
            variantFilter {
                if (name == "debug") {
                    setIgnore(true)
                }
            }
        }

        val sourcesJarTask = tasks.create<Jar>("sourcesJar") {
            classifier = "sources"
            from(
                this@subprojects.extensions.getByType(BaseExtension::class.java)
                    .sourceSets
                    .getByName("main")
                    .java
                    .srcDirs
            )
        }

        val installTask = tasks.getByName("install")

        (installTask as Upload).run {
            DslObject(repositories).convention
                .getPlugin(MavenRepositoryHandlerConvention::class.java)
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
        }

        artifacts {
            add("archives", sourcesJarTask)
        }

        // Release artifacts uploading
        apply<BintrayPlugin>()
        tasks.getByName("bintrayUpload").dependsOn(installTask)

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

        // Snapshot artifacts uploading
        apply<ArtifactoryPlugin>()
        tasks.getByName("artifactoryPublish").dependsOn(installTask)
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
                    invokeMethod("publishConfigs", "archives")
                })
            })
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "4.9"
    distributionType = Wrapper.DistributionType.BIN
}

val checkTask = task("check") {
    group = "verification"
    dependsOn("detektCheck")
}

task("build") {
    group = "build"
    dependsOn(checkTask)
}

task<Delete>("clean") {
    group = "build"
    delete("${rootProject.buildDir}")
}
