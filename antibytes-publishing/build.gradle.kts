import tech.antibytes.gradle.plugin.config.LibraryConfig

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.plugin.script.maven-package")
    id("tech.antibytes.gradle.plugin.script.publishing")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

object Version {
    const val kotlin = "1.5.31"
    const val junit = "5.8.1"
    const val publishing = "5.11.0.202103091610-r"
    const val versioning = "0.12.3"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}")
    implementation("org.eclipse.jgit:org.eclipse.jgit:${Version.publishing}")
    implementation("com.palantir.gradle.gitversion:gradle-git-version:${Version.versioning}")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}")
    testImplementation(platform("org.junit:junit-bom:${Version.junit}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
        version = "0.1.0"
    }
}
