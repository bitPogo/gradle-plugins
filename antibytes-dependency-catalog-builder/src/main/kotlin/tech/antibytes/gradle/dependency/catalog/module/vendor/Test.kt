/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
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
        private val group = "com.github.tschuchortdev"

        val core = MavenTestArtifact(
            group = group,
            id = "kotlin-compile-testing",
        )
        val ksp = MavenTestArtifact(
            group = group,
            id = "kotlin-compile-testing-ksp",
        )
    }
}
