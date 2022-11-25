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
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration
import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.Type

plugins {
    `kotlin-dsl`
    `java-library`

    id("tech.antibytes.gradle.coverage.local")
    id("tech.antibytes.gradle.publishing.local")
}

antiBytesPublishing {
    packaging.set(
        PackageConfiguration(
            type = Type.PURE_JAVA,
            pom = PomConfiguration(
                name = "antibytes-gradle-test-utils",
                description = "Helpers to make Gradle testable.",
                year = 2022,
                url = LibraryConfig.publishing.url,
            ),
            developers = listOf(
                DeveloperConfiguration(
                    id = LibraryConfig.publishing.developerId,
                    name = LibraryConfig.publishing.developerName,
                    url = LibraryConfig.publishing.developerUrl,
                    email = LibraryConfig.publishing.developerEmail,
                )
            ),
            license = LicenseConfiguration(
                name = LibraryConfig.publishing.licenseName,
                url = LibraryConfig.publishing.licenseUrl,
                distribution = LibraryConfig.publishing.licenseDistribution,
            ),
            scm = SourceControlConfiguration(
                url = LibraryConfig.publishing.scmUrl,
                connection = LibraryConfig.publishing.scmConnection,
                developerConnection = LibraryConfig.publishing.scmDeveloperConnection,
            ),
        )
    )
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(libs.mockk)
    implementation(libs.jvmFixture)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
        minimum = BigDecimal(0.45)
    )

    val jvmCoverage = JvmJacocoConfiguration.createJvmOnlyConfiguration(
        project,
        verificationRules = setOf(
            branchCoverage,
            instructionCoverage
        )
    )

    configurations.set(
        mapOf("jvm" to jvmCoverage)
    )
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {
    dependsOn("jvmCoverageVerification")
}
