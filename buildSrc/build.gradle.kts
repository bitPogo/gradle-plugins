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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    // Coverage
    implementation("org.jacoco:org.jacoco.core:0.8.7")
    // CVE
    implementation("org.owasp:dependency-check-gradle:6.5.3")
    // dependency-updates.gradle.kts
    implementation("com.github.ben-manes:gradle-versions-plugin:0.41.0")
    // publishing.gradle.kts
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r")
    // Versioning
    implementation("com.palantir.gradle.gitversion:gradle-git-version:0.12.3")
    // spotless
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.3.0")
    implementation("com.pinterest:ktlint:0.44.0")
}

with(extensions.getByType<JavaPluginExtension>()) {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
