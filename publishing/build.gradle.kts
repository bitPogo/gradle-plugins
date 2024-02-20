/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(libs.versions.java.get()).toString()))
    }

    sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
}

dependencies {
    implementation(libs.kotlin)
    implementation(libs.publishing)
    implementation(libs.versioning)
    implementation(projects.utilsPublishing)
    implementation(projects.versioningPublishing)
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.publishing.local") {
        id = "tech.antibytes.gradle.publishing.local"
        implementationClass = "tech.antibytes.gradle.publishing.AntibytesPublishing"
    }
}
