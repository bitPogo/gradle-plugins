import tech.antibytes.gradle.plugin.dependency.ensureKotlinVersion

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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    // Coverage
    implementation("org.jacoco:org.jacoco.core:0.8.8")
    // CVE
    implementation("org.owasp:dependency-check-gradle:7.1.1")
    // dependency-updates.gradle.kts
    implementation("com.github.ben-manes:gradle-versions-plugin:0.42.0")
    // publishing.gradle.kts
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.2.0.202206071550-r")
    // Versioning
    implementation("com.palantir.gradle.gitversion:gradle-git-version:0.15.0")
    // spotless
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.8.0")
    implementation("com.pinterest:ktlint:0.46.1")
}

with(extensions.getByType<JavaPluginExtension>()) {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
