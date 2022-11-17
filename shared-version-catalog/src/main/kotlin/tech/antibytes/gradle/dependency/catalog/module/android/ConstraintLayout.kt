/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.android

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object ConstraintLayout {
    private const val group = "androidx.constraintlayout"
    val core = MavenArtifact(
        group = group,
        id = "constraintlayout",
        platform = Platform.ANDROID,
    )

    val compose = MavenArtifact(
        group = group,
        id = "constraintlayout-compose",
        platform = Platform.ANDROID,
    )
}
