/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.plugin.dependency.Dependency

plugins {
    `kotlin-dsl`
    `java-library`
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(Dependency.gradle.kotlin)
    implementation(Dependency.test.mockk)
    implementation(Dependency.test.fixture)

    testImplementation(Dependency.test.kotlinTest)
    testImplementation(platform(Dependency.test.junit))
    testImplementation(Dependency.test.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
