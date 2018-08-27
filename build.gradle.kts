import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.BintrayPlugin
import org.gradle.api.internal.plugins.DslObject

plugins {
    `kotlin-dsl` apply false
    id("com.android.application") apply false
    id("com.github.dcendents.android-maven") version "2.1" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC8"
}

group = "com.avito.ui-testing"
version = project.properties["uiTestingVersion"] ?: "undefined"

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
        apply<BintrayPlugin>()

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
