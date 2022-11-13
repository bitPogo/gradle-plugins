/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.GradleBundle
import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.MavenTestArtifact
import tech.antibytes.gradle.dependency.Platform

internal object Hilt {
    private const val group = "com.google.dagger"

    val core = MavenArtifact(
        group = group,
        id = "hilt-android",
        platform = Platform.ANDROID,
    )
    val gradle = GradleBundle(
        group = group,
        id = "hilt-android-gradle-plugin",
        plugin = "dagger.hilt.android.plugin",
    )
    val compiler = MavenArtifact(
        group = group,
        id = "hilt-compiler",
        platform = Platform.ANDROID,
    )
    val test = MavenTestArtifact(
        group = group,
        id = "hilt-android-testing",
        platform = Platform.ANDROID,
    )

    val compose = MavenArtifact(
        group = group,
        id = "hilt-navigation-compose",
        platform = Platform.ANDROID,
    )
}
