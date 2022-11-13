/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.Platform

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
