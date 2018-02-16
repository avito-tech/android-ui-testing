buildscript {
    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = properties["kotlinVersion"].toString()))
        classpath("com.android.tools.build:gradle:3.1.1")
    }
}

subprojects {
    repositories {
        jcenter()
        google()
    }
}

task<Wrapper>("wrapper") {
    gradleVersion = "4.6"
    distributionType = Wrapper.DistributionType.BIN
}