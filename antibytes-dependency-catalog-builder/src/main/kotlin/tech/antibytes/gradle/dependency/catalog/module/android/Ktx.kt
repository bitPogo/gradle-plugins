/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object Ktx {
    val annotation = MavenArtifact(
        group = "androidx.annotation",
        id = "annotation",
        platform = Platform.ANDROID,
    )
    val core = MavenArtifact(
        group = "androidx.core",
        id = "core-ktx",
        platform = Platform.ANDROID,
    )

    val activity = Activity
    internal object Activity {
        private const val group = "androidx.activity"
        val core = MavenArtifact(
            group = group,
            id = "activity-ktx",
            platform = Platform.ANDROID,
        )
        val compose = MavenArtifact(
            group = group,
            id = "activity-compose",
            platform = Platform.ANDROID,
        )
    }

    val collections = MavenArtifact(
        group = "androidx.collections",
        id = "collections-ktx",
        platform = Platform.ANDROID,
    )
    val fragment = MavenArtifact(
        group = "androidx.fragment",
        id = "fragment-ktx",
        platform = Platform.ANDROID,
    )
    val lifecycle = MavenArtifact(
        group = "androidx.lifecycle",
        id = "lifecycle-runtime-ktx",
        platform = Platform.ANDROID,
    )

    val navigation = Navigation

    internal object Navigation {
        private const val group = "androidx.navigation"
        val runtime = MavenArtifact(
            group = group,
            id = "navigation-runtime-ktx",
            platform = Platform.ANDROID,
        )
        val fragment = MavenArtifact(
            group = group,
            id = "navigation-fragment-ktx",
            platform = Platform.ANDROID,
        )
        val ui = MavenArtifact(
            group = group,
            id = "navigation-ui-ktx",
            platform = Platform.ANDROID,
        )
        val compose = MavenArtifact(
            group = group,
            id = "navigation-compose",
            platform = Platform.ANDROID,
        )
    }

    val palette = MavenArtifact(
        group = "androidx.palette",
        id = "palette-ktx",
        platform = Platform.ANDROID,
    )
    val paging = Paging

    internal object Paging {
        private const val group = "androidx.paging"
        val core = MavenArtifact(
            group = group,
            id = "paging-runtime",
            platform = Platform.ANDROID,
        )
        val compose = MavenArtifact(
            group = group,
            id = "paging-compose",
            platform = Platform.ANDROID,
        )
    }

    val viewmodel = Viewmodel

    internal object Viewmodel {
        private const val group = "androidx.lifecycle"
        val core = MavenArtifact(
            group = group,
            id = "lifecycle-viewmodel-ktx",
            platform = Platform.ANDROID,
        )
        val saver = MavenArtifact(
            group = group,
            id = "lifecycle-viewmodel-savedstate",
            platform = Platform.ANDROID,
        )
        val compose = MavenArtifact(
            group = group,
            id = "lifecycle-viewmodel-compose",
            platform = Platform.ANDROID,
        )
    }

    val workmanager = MavenArtifact(
        group = "androidx.work",
        id = "work-runtime-ktx",
        platform = Platform.ANDROID,
    )

    val splashscreen = MavenArtifact(
        group = "androidx.core",
        id = "core-splashscreen",
        platform = Platform.ANDROID,
    )
}
