/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.kotlin)
    // Coverage
    implementation(libs.jacoco)
    // CVE
    implementation(libs.owasp)
    // dependency-updates.gradle.kts
    implementation(libs.dependencyUpdate)
    // publishing.gradle.kts
    implementation(libs.publishing)
    // Versioning
    implementation(libs.versioning)
    // spotless
    implementation(libs.spotless)
    implementation(libs.ktlint)
}

with(extensions.getByType<JavaPluginExtension>()) {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
