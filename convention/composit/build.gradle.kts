/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.configuration.java.local")
    id("tech.antibytes.gradle.runtime.local")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.kotlin)
    implementation(libs.agp)
    implementation(libs.jacoco)
    implementation(libs.versioning)
    implementation(libs.publishing)
    implementation(libs.spotless)
    implementation(libs.ktlint)
    implementation(libs.detekt)
    implementation(libs.sonarqube)
    implementation(libs.stableApi)
    implementation(libs.dependencyUpdate)
    implementation(libs.owasp)
    implementation(libs.mavenArtifacts)
    implementation(libs.kotlinPoet)
}

gradlePlugin {
    plugins {
        register("tech.antibytes.gradle.versioning.local") {
            id = "tech.antibytes.gradle.versioning.local"
            implementationClass = "tech.antibytes.gradle.versioning.AntibytesVersioning"
        }
        register("tech.antibytes.gradle.coverage.local") {
            id = "tech.antibytes.gradle.coverage.local"
            implementationClass = "tech.antibytes.gradle.coverage.AntibytesCoverage"
        }
        register("tech.antibytes.gradle.component.local") {
            id = "tech.antibytes.gradle.component.local"
            implementationClass = "tech.antibytes.gradle.component.AntibytesCustomComponent"
            displayName = "${id}.gradle.plugin"
            description = "Publish custom components/artifacts for Antibytes projects."
            version = "0.1.0"
        }
        register("tech.antibytes.gradle.dependency.helper.local") {
            id = "tech.antibytes.gradle.dependency.helper.local"
            implementationClass = "tech.antibytes.gradle.dependency.helper.AntibytesDependencyHelper"
        }
        register("tech.antibytes.gradle.dependency.local") {
            id = "tech.antibytes.gradle.dependency.local"
            implementationClass = "tech.antibytes.gradle.dependency.BridgePlugin"
        }
        register("tech.antibytes.gradle.quality.local") {
            id = "tech.antibytes.gradle.quality.local"
            implementationClass = "tech.antibytes.gradle.quality.AntibytesQualityLocal"
        }
        register("tech.antibytes.gradle.publishing.local") {
            id = "tech.antibytes.gradle.publishing.local"
            implementationClass = "tech.antibytes.gradle.publishing.AntibytesPublishing"
        }
    }
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "src-plugin/main/kotlin",
            "build/generated/antibytes/main/kotlin",
        )
    }
}

val provideCoverageConfig: AntiBytesMainConfigurationTask by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.coverage.config")
    stringFields.set(
        mapOf(
            "jacoco" to libs.versions.jacoco.get(),
        )
    )
}

val provideQualityConfig: AntiBytesMainConfigurationTask by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.quality.config")

    val versions = mutableMapOf(
        "javaVersion" to libs.versions.java.jvm.get(),
        "detektVersion" to libs.versions.detekt.get(),
        "ktlintVersion" to libs.versions.ktlint.get(),
        "remoteDetektConfig" to antibytes.gradle.antibytes.detektConfiguration.get().toString(),
    )

    stringFields.set(versions)
}

val provideDependencyHelperConfig: AntiBytesMainConfigurationTask by tasks.creating(AntiBytesMainConfigurationTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.dependency.config")
    stringFields.set(
        mapOf(
            "kotlin" to libs.versions.kotlin.get(),
        )
    )
}

tasks.withType<KotlinCompile> {
    dependsOn(
        provideCoverageConfig,
        provideQualityConfig,
        provideDependencyHelperConfig,
    )
}
