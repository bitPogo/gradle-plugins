/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.plugin.config.LibraryConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tech.antibytes.gradle.local.AntibytesDependencyVersionTask

plugins {
    `kotlin-dsl`
    `version-catalog`

    id("tech.antibytes.gradle.local")
}

// To make it available as direct dependency
group = LibraryConfig.PublishConfig.groupId

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

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "src-gen/main/kotlin",
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

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    dependsOn(
        // provideConfig,
        provideVersions,
    )
}
