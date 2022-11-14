/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.Platform

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
