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
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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
