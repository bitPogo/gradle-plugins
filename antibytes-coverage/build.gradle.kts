/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCounter
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoMeasurement

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.plugin.script.maven-package")

    id("tech.antibytes.gradle.coverage.local")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(libs.agp)
    implementation(libs.jacoco)
    implementation(project(":antibytes-gradle-utils"))

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
    testImplementation(project(":antibytes-gradle-test-utils"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("${LibraryConfig.group}.coverage") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.gradle.coverage"
        displayName = "${id}.gradle.plugin"
        implementationClass = "tech.antibytes.gradle.coverage.AntiBytesCoverage"
        description = "Coverage Plugin for Kotlin Multiplatform project of Antibytes"
    }
}

antiBytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.99)
    )

    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.95)
    )

    val jvmCoverage = JvmJacocoConfiguration.createJvmOnlyConfiguration(
        project,
        verificationRules = setOf(
            branchCoverage,
            instructionCoverage
        )
    )

    configurations["jvm"] = jvmCoverage
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {
    dependsOn("jvmCoverageVerification")
}
