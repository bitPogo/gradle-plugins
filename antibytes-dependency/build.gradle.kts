/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
<<<<<<< HEAD
=======
import org.gradle.api.tasks.testing.logging.TestLogEvent
>>>>>>> 235e514 (Add Versions)

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    jacoco

    id("tech.antibytes.gradle.plugin.script.maven-package")
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}
// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(tech.antibytes.gradle.plugin.dependency.Dependency.gradle.atomicFu) {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation(tech.antibytes.gradle.plugin.dependency.Dependency.gradle.android)

    testImplementation(tech.antibytes.gradle.plugin.dependency.Dependency.test.kotlinTest)
    testImplementation(platform(tech.antibytes.gradle.plugin.dependency.Dependency.test.junit))
    testImplementation(tech.antibytes.gradle.plugin.dependency.Dependency.test.mockk)
    testImplementation(tech.antibytes.gradle.plugin.dependency.Dependency.test.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}
