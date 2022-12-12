/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.versioning.api.VersioningConfiguration
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
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.coverage.local")
    id("tech.antibytes.gradle.publishing.local")
}

antiBytesPublishing {
    versioning.set(
        VersioningConfiguration(
            featurePrefixes = listOf("feature")
        )
    )
    packaging.set(
        PackageConfiguration(
            groupId = LibraryConfig.PublishConfig.groupId,
            pom = PomConfiguration(
                name = "antibytes-custom-component",
                description = "Publish custom components/artifacts for Antibytes projects.",
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
    repositories.set(
        setOf(
            GitRepositoryConfiguration(
                name = "Development",
                gitWorkDirectory = "dev",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-dev",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            ),
            GitRepositoryConfiguration(
                name = "Snapshot",
                gitWorkDirectory = "snapshots",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-snapshots",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            ),
            GitRepositoryConfiguration(
                name = "RollingRelease",
                gitWorkDirectory = "rolling",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-rolling-releases",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            ),
            GitRepositoryConfiguration(
                name = "Release",
                gitWorkDirectory = "releases",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-releases",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            )
        )
    )
}

antiBytesCoverage {
    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.89)
    )

    val jvmCoverage = JvmJacocoConfiguration.createJvmOnlyConfiguration(
        project,
        verificationRules = setOf(instructionCoverage)
    )

    configurations.set(
        mapOf("jvm" to jvmCoverage)
    )
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

dependencies {
    implementation(libs.kotlin)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
    testImplementation(libs.jupiter)
    testImplementation(project(":antibytes-gradle-test-utils"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

gradlePlugin {
    plugins.register("${LibraryConfig.group}.gradle.component") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.gradle.component"
        implementationClass = "tech.antibytes.gradle.component.AntiBytesCustomComponent"
        displayName = "${id}.gradle.plugin"
        description = "Publish custom components/artifacts for Antibytes projects."
        version = "0.1.0"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {
    dependsOn("jvmCoverageVerification")
}
