/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
import tech.antibytes.gradle.plugin.config.LibraryConfig
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent
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
import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("idea")

    id("tech.antibytes.gradle.coverage.local")
    id("tech.antibytes.gradle.publishing.local")
}

val pluginId = "${LibraryConfig.group}.grammar"
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
            pom = PomConfiguration(
                name = name,
                description = "A Bison and JFlex plugin for Gradle.",
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

// To make it available as direct dependency
group = "tech.antibytes.gradle.grammar"

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output

        java.srcDir("src/integration/kotlin")
        resources.srcDir("src/integration/resources")
    }
}

idea {
    module {
        project.sourceSets["integrationTest"].java.srcDirs.forEach { src ->
            testSources.from(src)
        }
        project.sourceSets["integrationTest"].resources.srcDirs.forEach { src ->
            testResources.from(src)
        }
    }
}

fun DependencyHandler.integrationTestImplementation(dependencyNotation: Any): Dependency? =
    add("integrationTestImplementation", dependencyNotation)

dependencies {
    implementation(libs.jflex)
    implementation(libs.turtle)

    testImplementation(libs.kotlinTest)
    testImplementation(platform(libs.junit))
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.jvmFixture)
    testImplementation(project(":antibytes-gradle-test-utils"))
    // Note: this is not necessary for tests itself, only for ide support of integrationTests
    testImplementation(gradleTestKit())

    integrationTestImplementation(libs.kotlinTest)
    integrationTestImplementation(platform(libs.junit))
    integrationTestImplementation(libs.jupiter)
    integrationTestImplementation(gradleTestKit())
}

gradlePlugin {
    plugins.create(pluginId) {
        id = pluginId
        displayName = "A Bison and JFlex plugin for Gradle and Kotlin."
        implementationClass = "tech.antibytes.gradle.grammar.GrammarToolsPlugin"
        description = "A Bison and JFlex plugin for Gradle and Kotlin."
    }
    testSourceSets(sourceSets.getByName("integrationTest"))
}

antibytesCoverage {
    val branchCoverage = JacocoVerificationRule(
        counter = JacocoCounter.BRANCH,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.99)
    )

    val instructionCoverage = JacocoVerificationRule(
        counter = JacocoCounter.INSTRUCTION,
        measurement = JacocoMeasurement.COVERED_RATIO,
        minimum = BigDecimal(0.99)
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

val provideBisonExec by tasks.creating(Task::class.java) {
    doFirst {
        val exec = project.findProperty("bison.exec")
        val bisonExec = File("${projectDir.absolutePath.trimEnd('/')}/src/integration/resources/bisonExec")
        if (!bisonExec.exists()) {
            bisonExec.createNewFile()
        }

        if (exec is String) {
            bisonExec.writeText(exec)
        } else {
            bisonExec.writeText("/usr/bin/bison")
        }
    }
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn(provideBisonExec)
}

val integrationTests by tasks.creating(Test::class.java) {
    description = "Run integration tests"
    group = "Verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}

tasks.check {
    dependsOn(integrationTests, "jvmCoverageVerification")
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()

    testLogging {
        events(TestLogEvent.FAILED)
    }
}
