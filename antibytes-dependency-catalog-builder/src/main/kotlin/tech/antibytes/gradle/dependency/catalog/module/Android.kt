/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.GradleArtifact
import tech.antibytes.gradle.dependency.catalog.GradlePlugin
import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.VersionlessGradlePlugin
import tech.antibytes.gradle.dependency.catalog.module.android.AppCompact
import tech.antibytes.gradle.dependency.catalog.module.android.Coil
import tech.antibytes.gradle.dependency.catalog.module.android.Compose
import tech.antibytes.gradle.dependency.catalog.module.android.ConstraintLayout
import tech.antibytes.gradle.dependency.catalog.module.android.Hilt
import tech.antibytes.gradle.dependency.catalog.module.android.Ktx
import tech.antibytes.gradle.dependency.catalog.module.android.Material
import tech.antibytes.gradle.dependency.catalog.module.android.Material3
import tech.antibytes.gradle.dependency.catalog.module.android.Test

internal object Android {
    // AGP
    val agp = GradleArtifact(
        group = "com.android.tools.build",
        id = "gradle",
    )

    val library = GradlePlugin("com.android.library")
    val application = GradlePlugin("com.android.application")

    val appCompact = AppCompact
    val coil = Coil
    val compose = Compose

    val constraintLayout = ConstraintLayout

    val desugar = MavenArtifact(
        group = "com.android.tools",
        id = "desugar_jdk_libs",
        platform = Platform.ANDROID,
    )

    val hilt = Hilt

    val ktx = Ktx

    val material = Material
    val material3 = Material3
    val test = Test
}
