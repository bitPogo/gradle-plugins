/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.vendor

import tech.antibytes.gradle.dependency.GradleBundle
import tech.antibytes.gradle.dependency.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.MavenTestArtifact
import tech.antibytes.gradle.dependency.MavenVersionlessTestArtifact
import tech.antibytes.gradle.dependency.Platform

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
}
