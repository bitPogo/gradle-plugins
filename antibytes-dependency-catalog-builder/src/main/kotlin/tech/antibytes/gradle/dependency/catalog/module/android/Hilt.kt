/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.GradleBundle
import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

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
        group = "androidx.hilt",
        id = "hilt-navigation-compose",
        platform = Platform.ANDROID,
    )
}
