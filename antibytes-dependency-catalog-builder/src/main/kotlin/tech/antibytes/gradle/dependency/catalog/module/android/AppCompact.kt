/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object AppCompact {
    private const val group = "androidx.appcompat"
    val core = MavenArtifact(
        group = group,
        id = "appcompat",
        platform = Platform.ANDROID,
    )
    val resources = MavenArtifact(
        group = group,
        id = "appcompat-resources",
        platform = Platform.ANDROID,
    )
}
