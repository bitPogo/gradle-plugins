import tech.antibytes.gradle.plugin.dependency.ensureKotlinVersion
import tech.antibytes.gradle.plugin.dependency.Dependency

/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

plugins {
    `kotlin-dsl`

    id("tech.antibytes.gradle.plugin.dependency")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(Dependency.gradle.kotlin)
    // Coverage
    implementation(Dependency.gradle.jacoco)
    // CVE
    implementation(Dependency.gradle.owasp)
    // dependency-updates.gradle.kts
    implementation(Dependency.gradle.dependencyUpdate)
    // publishing.gradle.kts
    implementation(Dependency.gradle.publishing)
    // Versioning
    implementation(Dependency.gradle.versioning)
    // spotless
    implementation(Dependency.gradle.spotless)
    implementation(Dependency.gradle.ktlint)
}

with(extensions.getByType<JavaPluginExtension>()) {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
