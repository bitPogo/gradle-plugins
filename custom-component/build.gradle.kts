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
    implementation(libs.kotlin)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(libs.versions.java.get()).toString()))
    }

    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.component.local") {
        id = "tech.antibytes.gradle.component.local"
        implementationClass = "tech.antibytes.gradle.component.AntibytesCustomComponent"
        displayName = "${id}.gradle.plugin"
        description = "Publish custom components/artifacts for Antibytes projects."
        version = "0.1.0"
    }
}

tasks.test {
    useJUnitPlatform()
}
