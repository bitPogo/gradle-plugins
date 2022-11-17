/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.MavenTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object Test {
    private const val group = "androidx.test"

    val core = MavenTestArtifact(
        group = group,
        id = "core",
        platform = Platform.ANDROID,
    )
    val ktx = MavenTestArtifact(
        group = group,
        id = "core-ktx",
        platform = Platform.ANDROID,
    )
    val runner = MavenTestArtifact(
        group = group,
        id = "runner",
        platform = Platform.ANDROID,
    )
    val rules = MavenTestArtifact(
        group = group,
        id = "rules",
        platform = Platform.ANDROID,
    )
    val uiAutomator = MavenTestArtifact(
        group = "$group.uiautomator",
        id = "uiautomator",
        platform = Platform.ANDROID,
    )
    val robolectric = MavenTestArtifact(
        group = "org.robolectric",
        id = "robolectric",
        platform = Platform.ANDROID,
    )

    val compose = Compose

    internal object Compose {
        private const val group = "androidx.compose.ui"

        val core = MavenTestArtifact(
            group = group,
            id = "ui-test",
            platform = Platform.ANDROID,
        )
        val manifest = MavenTestArtifact(
            group = group,
            id = "ui-test-manifest",
            platform = Platform.ANDROID,
        )
        val junit4 = MavenTestArtifact(
            group = group,
            id = "ui-test-junit4",
            platform = Platform.ANDROID,
        )
    }

    val espresso = Espresso

    internal object Espresso {
        private const val group = "${Test.group}.espresso"
        val core = MavenTestArtifact(
            group = group,
            id = "espresso-core",
            platform = Platform.ANDROID,
        )
        val intents = MavenTestArtifact(
            group = group,
            id = "espresso-intents",
            platform = Platform.ANDROID,
        )
        val web = MavenTestArtifact(
            group = group,
            id = "espresso-web",
            platform = Platform.ANDROID,
        )
    }

    val junit = JUnit

    internal object JUnit {
        private const val group = "${Test.group}.ext"
        val core = MavenTestArtifact(
            group = group,
            id = "junit",
            platform = Platform.ANDROID,
        )
        val ktx = MavenTestArtifact(
            group = group,
            id = "junit-ktx",
            platform = Platform.ANDROID,
        )
    }
}
