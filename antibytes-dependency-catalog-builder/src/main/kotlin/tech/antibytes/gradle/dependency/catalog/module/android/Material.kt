/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object Material {
    private const val group = "com.google.android.material"

    val core = MavenArtifact(
        group = group,
        id = "material",
        platform = Platform.ANDROID,
    )
    val compose = Compose

    internal object Compose {
        private const val group = "androidx.compose.material"

        val core = MavenArtifact(
            group = group,
            id = "material",
            platform = Platform.ANDROID,
        )
        val icons = MavenArtifact(
            group = group,
            id = "material-icons-core",
            platform = Platform.ANDROID,
        )
        val extendedIcons = MavenArtifact(
            group = group,
            id = "material-icons-extended",
            platform = Platform.ANDROID,
        )
        val ripple = MavenArtifact(
            group = group,
            id = "material-ripple",
            platform = Platform.ANDROID,
        )
    }
}
