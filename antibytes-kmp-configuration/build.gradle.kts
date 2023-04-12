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
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.publishing.local")
    id("tech.antibytes.gradle.coverage.local")
}

val pluginId = "${LibraryConfig.group}.configuration.kmp"
val versioningConfiguration = VersioningConfiguration(
    featurePrefixes = listOf("feature"),
    suppressSnapshot = true
)

// To make it available as direct dependency
group = pluginId

antibytesVersioning {
    configuration = versioningConfiguration
}

antibytesPublishing {
    versioning.set(versioningConfiguration)
    packaging.set(
        PackageConfiguration(
            groupId = pluginId,
            pom = PomConfiguration(
                name = name,
                description = "KMP Configuration for Antibytes projects.",
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
            ),
            MavenRepositoryConfiguration(
                name = "Local",
                url = uri(rootProject.layout.buildDirectory),
            ),
        )
    )
}

dependencies {
    implementation(libs.kotlin)
    implementation(project(":antibytes-gradle-utils"))

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.mockkInstumented)
    testImplementation(libs.jvmFixture)
    testImplementation(project(":antibytes-gradle-test-utils"))
}

gradlePlugin {
    plugins.create(pluginId) {
        id = pluginId
        displayName = "Configuration Plugin for Antibytes KMP projects."
        implementationClass = "tech.antibytes.gradle.configuration.AntibytesKmpConfiguration"
        description = "KMP Configuration for Antibytes projects."
    }
}

antibytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.95)
    )

    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.87)
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
