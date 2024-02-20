/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.gson)
    implementation(libs.kotlinPoet)
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "src-plugin/main/kotlin",
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(libs.versions.java.get()).toString()))
    }

    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.dependency.local") {
        id = "tech.antibytes.gradle.dependency.local"
        implementationClass = "tech.antibytes.gradle.dependency.DependencyPlugin"
    }
}

tasks.test {
    useJUnitPlatform()
}
