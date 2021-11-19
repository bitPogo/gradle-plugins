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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")

    // Coverage
    implementation("org.jacoco:org.jacoco.core:0.8.7")
    // CVE
    implementation("org.owasp:dependency-check-gradle:6.4.1")

    // dependency-updates.gradle.kts
    implementation("com.github.ben-manes:gradle-versions-plugin:0.39.0")
    // publishing.gradle.kts
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.11.0.202103091610-r")
    // Versioning
    implementation("com.palantir.gradle.gitversion:gradle-git-version:0.12.3")
    // spotless
    implementation("com.diffplug.spotless:spotless-plugin-gradle:5.14.3")
    implementation("com.pinterest:ktlint:0.42.1")
}
