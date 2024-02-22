/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesIntegrationTestConfigurationTask
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask
import tech.antibytes.gradle.configuration.runtime.AntiBytesTestConfigurationTask
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
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.versioning.Versioning

plugins {
    `kotlin-dsl`
    `java-library`

    id("com.palantir.git-version")
    id("tech.antibytes.gradle.runtime.local")
    id("tech.antibytes.gradle.coverage.local")
    id("tech.antibytes.gradle.publishing.local")
}

val pluginId = "${LibraryConfig.group}.versioning"
val versioningConfiguration = VersioningConfiguration(
    featurePrefixes = emptyList(),
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
            type = Type.PURE_JAVA,
            pom = PomConfiguration(
                name = name,
                description = "Autoversioning for Antibytes Projects.",
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

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output

        java.srcDirs(
            "src/integrationTest/kotlin",
            "build/generated/antibytes/integrationTest/kotlin",
        )
        resources.srcDir("src/integrationTest/resources")
    }
}

fun DependencyHandler.integrationTestImplementation(dependencyNotation: Any): Dependency? =
    add("integrationTestImplementation", dependencyNotation)

dependencies {
    implementation(libs.versioning)
    implementation(libs.turtle)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
    testImplementation(projects.antibytesGradleTestUtils)

    integrationTestImplementation(libs.kotlinTest5)
    integrationTestImplementation(platform(libs.junit))
    integrationTestImplementation(libs.jupiter)
    integrationTestImplementation(gradleTestKit())
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

val versionDetails: groovy.lang.Closure<com.palantir.gradle.gitversion.VersionDetails> by extra
val details = versionDetails()

val provideConfig: AntiBytesIntegrationTestConfigurationTask by tasks.creating(AntiBytesIntegrationTestConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.versioning")
    stringFields.set(
        mapOf(
            "lastTag" to details.lastTag,
            "gitHash" to details.gitHash,
            "gitHashFull" to details.gitHashFull,
            "branchName" to details.branchName
        )
    )
    integerFields.set(
        mapOf(
            "commitDistance" to details.commitDistance,
        )
    )
    booleanFields.set(
        mapOf(
            "isCleanTag" to details.isCleanTag
        )
    )
}

val integrationTests by tasks.creating(Test::class.java) {
    description = "Run integration tests"
    group = "Verification"

    dependsOn(provideConfig)
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}

tasks.check {
    dependsOn(integrationTests, "jvmCoverageVerification")
}

tasks.withType(Test::class.java) {
    testLogging {
        events(TestLogEvent.FAILED)
    }
}

gradlePlugin {
    plugins.create(pluginId) {
        id = pluginId
        implementationClass = "tech.antibytes.gradle.versioning.AntibytesVersioning"
        displayName = "Setup for Antibytes Versioning."
        description = "Setup for Antibytes Versioning."
    }
    testSourceSets(sourceSets.getByName("integrationTest"))
}
