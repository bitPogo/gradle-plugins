/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object Material3 {
    private const val group = "androidx.compose.material3"

    val core = MavenArtifact(
        group = group,
        id = "material3",
        platform = Platform.ANDROID,
    )
    val windowSize = MavenArtifact(
        group = group,
        id = "material3-window-size-class",
        platform = Platform.ANDROID,
    )
}
