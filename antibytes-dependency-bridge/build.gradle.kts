/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig

plugins {
    `kotlin-dsl`
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(libs.gson)
    implementation(libs.kotlinPoet)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
    testImplementation(libs.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}
