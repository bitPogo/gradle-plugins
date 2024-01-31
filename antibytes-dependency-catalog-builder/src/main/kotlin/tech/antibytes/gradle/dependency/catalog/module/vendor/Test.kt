/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.vendor

import tech.antibytes.gradle.dependency.catalog.GradleBundle
import tech.antibytes.gradle.dependency.catalog.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.catalog.MavenTestArtifact
import tech.antibytes.gradle.dependency.catalog.MavenVersionlessTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object Test {
    val mockk = MavenKmpTestArtifact(
        group = "io.mockk",
        id = "mockk",
        platforms = listOf(
            Platform.COMMON,
            Platform.ANDROID,
            Platform.JVM,
        ),
    )

    val paparazzi = GradleBundle(
        group = "app.cash.paparazzi",
        id = "paparazzi-gradle-plugin",
        plugin = "app.cash.paparazzi",
    )

    val roborazzi = GradleBundle(
        group = "io.github.takahirom",
        id = "roborazzi-gradle-plugin",
        plugin = "io.github.takahirom.roborazzi",
    )

    val turbine = MavenKmpTestArtifact(
        group = "app.cash.turbine",
        id = "turbine",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
            Platform.MACOS_ARM64,
            Platform.MACOS_X64,
            Platform.TVOS_ARM64,
            Platform.TVOS_SIMULATOR_ARM64,
            Platform.TVOS_X64,
            Platform.WATCHOS_ARM32,
            Platform.WATCHOS_ARM64,
            Platform.WATCHOS_SIMULATOR_ARM64,
            Platform.WATCHOS_X64,
            Platform.WATCHOS_X86,
        ),
    )

    val junit = JUnit

    internal object JUnit {
        private const val junitGroup = "org.junit"
        val bom = MavenTestArtifact(
            group = junitGroup,
            id = "junit-bom",
        )
        val core = MavenTestArtifact(
            group = "$junitGroup.jupiter",
            id = "junit-jupiter-api",
        )
        val parameterized = MavenTestArtifact(
            group = "$junitGroup.jupiter",
            id = "junit-jupiter-params",
        )
        val legacyEngineJunit4 = MavenTestArtifact(
            group = "$junitGroup.vintage",
            id = "junit-vintage-engine",
        )
        val runtime = MavenVersionlessTestArtifact(
            group = "$junitGroup.jupiter",
            id = "junit-jupiter",
        )

        /**
         * [JUnit4](https://github.com/junit-team/junit4/)
         */
        val junit4 = MavenTestArtifact(
            group = "junit",
            id = "junit",
        )
    }

    val compiler = Compiler

    internal object Compiler {
        private val group = "dev.zacsweers.kctfork"

        val core = MavenTestArtifact(
            group = group,
            id = "core",
        )
        val ksp = MavenTestArtifact(
            group = group,
            id = "ksp",
        )
    }
}
