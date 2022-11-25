/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask
import tech.antibytes.gradle.coverage.api.JacocoVerificationRule
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCounter
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoMeasurement
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration
import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.runtime.local")
    id("tech.antibytes.gradle.coverage.local")
    id("tech.antibytes.gradle.publishing.local")
}

antiBytesPublishing {
    packaging.set(
        PackageConfiguration(
            pom = PomConfiguration(
                name = "antibytes-dependency-helper",
                description = "Helpers to bridge TOMLs to Dependencies and to keep repos up to date.",
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
    implementation(libs.kotlin)
    implementation(libs.dependencyUpdate)
    implementation(libs.owasp)
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
    plugins.register("${LibraryConfig.group}.gradle.dependency") {
        group = LibraryConfig.group
        id = "${LibraryConfig.group}.gradle.dependency"
        implementationClass = "tech.antibytes.gradle.dependency.AntiBytesDependencyHelper"
        displayName = "${id}.gradle.plugin"
        description = "Dependency Helper for Antibytes projects"
    }
}

antiBytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.96)
    )

    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.85)
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

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin"
        )
    }
}

val provideConfig: AntiBytesMainConfigurationTask by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.dependency.config")
    stringFields.set(
        mapOf(
            "kotlin" to libs.versions.kotlin.get(),
        )
    )
}

tasks.withType<KotlinCompile> {
    dependsOn(provideConfig)
}

tasks.test {
    useJUnitPlatform()
}

tasks.check {
    dependsOn("jvmCoverageVerification")
}
