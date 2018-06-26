import com.android.build.api.dsl.extension.AndroidExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryPlugin
import com.android.builder.model.AndroidLibrary
import com.jfrog.bintray.gradle.BintrayExtension
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
        classpath("com.android.tools.build:gradle:3.1.1")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.0")
    }
}

group = "com.avito.ui-testing"
version = "0.2.1"

val minSdk: String by project
val targetSdk: String by project

subprojects.forEach { subProject: Project ->
    subProject.repositories {
        jcenter()
        google()
    }

    subProject.group = rootProject.group
    subProject.version = rootProject.version

    subProject.plugins.withType(LibraryPlugin::class.java) {

        subProject.apply {
            plugin("com.github.dcendents.android-maven")
            plugin("com.jfrog.bintray")
        }

        val sourcesJarTask = subProject.tasks.create<Jar>("sourcesJar") {
            classifier = "sources"
            from(subProject.extensions.getByType<BaseExtension>().sourceSets.getByName("main").java.srcDirs)
        }

        val installTask = subProject.tasks.getByName("install")

        (installTask as Upload).run {
            DslObject(repositories).convention.getPlugin(MavenRepositoryHandlerConvention::class.java).mavenInstaller {
                pom {
                    project {
                        packaging = "aar"
                        groupId = rootProject.group.toString()
                        artifactId = subProject.name
                        version = rootProject.version.toString()
                    }
                }
            }
        }

        subProject.tasks.getByName("bintrayUpload").dependsOn(installTask)

        subProject.artifacts {
            add("archives", sourcesJarTask)
        }

        subProject.extensions.getByType<BintrayExtension>().run {
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

task<Wrapper>("wrapper") {
    gradleVersion = "4.8.1"
    distributionType = Wrapper.DistributionType.BIN
}

task<Delete>("clean") { delete("${rootProject.buildDir}") }

inline fun <reified T> ExtensionContainer.getByType(): T = getByType(T::class.java)

inline fun <reified T : Task> TaskContainer.create(name: String, noinline configure: T.() -> Unit): T = create(name, T::class.java, configure)