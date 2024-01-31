/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object Coil {
    private const val group = "io.coil-kt"
    val core = MavenArtifact(
        group = group,
        id = "coil",
        platform = Platform.ANDROID,
    )
    val compose = MavenArtifact(
        group = group,
        id = "coil-compose",
        platform = Platform.ANDROID,
    )
}
