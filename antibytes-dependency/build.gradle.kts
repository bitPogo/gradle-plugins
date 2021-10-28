import tech.antibytes.gradle.plugin.config.LibraryConfig

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.plugin.script.maven-package")
    id("tech.antibytes.gradle.plugin.script.publishing")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId
version = "0.1.0"

object Version {
    const val kotlin = "1.5.31"
    const val junit = "4.13.2"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}")
    testImplementation("junit:junit:${Version.junit}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins.register("antibytes-dependency") {
        group = LibraryConfig.group
        id = "antibytes-dependency"
        implementationClass = "tech.antibytes.gradle.dependency.AntiBytesDependency"
        description = "General dependencies for Antibytes projects"
    }
}
