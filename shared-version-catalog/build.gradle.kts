/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.local.AntibytesDependencyVersionTask
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask


plugins {
    `kotlin-dsl`
    `java-gradle-plugin`

    id("tech.antibytes.gradle.runtime.local")
    id("tech.antibytes.gradle.dependency.local")
    id("tech.antibytes.gradle.versioning.local")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin",
            "src-plugin/main/kotlin",
        )
    }
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
                )
            ).versionName(),
        )
    )
}

val provideVersions: AntibytesDependencyVersionTask by tasks.creating(AntibytesDependencyVersionTask::class.java) {
    mustRunAfter("clean")

    packageName.set("tech.antibytes.gradle.dependency.config")
    val externalDependencies = listOf(File("${projectDir.absolutePath.trimEnd('/')}/../shared-dependencies"))
    val internalDependencies = listOf(File("${projectDir.absolutePath.trimEnd('/')}/../gradle"))

    pythonDirectory.set(externalDependencies)
    nodeDirectory.set(externalDependencies)
    gradleDirectory.set(
        listOf(
            externalDependencies,
            internalDependencies,
        ).flatten()
    )
}

tasks.withType<KotlinCompile> {
    dependsOn(
        provideVersions,
        provideConfig,
    )
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.dependency.catalog") {
        id = "tech.antibytes.gradle.dependency.catalog"
        implementationClass = "tech.antibytes.gradle.dependency.catalog.DependencyPlugin"
    }
}
