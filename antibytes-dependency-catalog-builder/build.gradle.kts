/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import tech.antibytes.gradle.local.AntibytesDependencyVersionTask
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCounter
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoMeasurement

plugins {
    `kotlin-dsl`
    `version-catalog`

    id("tech.antibytes.gradle.runtime.local")
    id("tech.antibytes.gradle.dependency.local")
    id("tech.antibytes.gradle.versioning.local")
    id("tech.antibytes.gradle.coverage.local")
}

val provideVersions: AntibytesDependencyVersionTask by tasks.creating(AntibytesDependencyVersionTask::class.java) {
    packageName.set("tech.antibytes.gradle.dependency.config")
    val externalDependencies = listOf(File("${projectDir.absolutePath.trimEnd('/')}/../shared-dependencies"))
    val internalDependencies = listOf(File("${projectDir.absolutePath.trimEnd('/')}/../gradle"))

    pythonDirectory.set(externalDependencies)
    nodeDirectory.set(externalDependencies)
    gradleDirectory.set(
        listOf(externalDependencies, internalDependencies).flatten()
    )
}

val provideConfig: AntiBytesMainConfigurationTask by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.dependency.config")
    stringFields.set(
        mapOf(
            "antibytes" to Versioning.getInstance(
                project = project,
                configuration = VersioningConfiguration(
                    featurePrefixes = listOf("feature"),
                    suppressSnapshot = true,
                )
            ).versionName(),
        )
    )
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin",
        )
    }
}

dependencies {
    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    dependsOn(
        provideVersions,
        provideConfig,
    )
}

antibytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.97)
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

    configurations.set(
        mapOf("jvm" to jvmCoverage)
    )
}

tasks.check {
    dependsOn("jvmCoverageVerification")
}

tasks.test {
    useJUnitPlatform()
}
