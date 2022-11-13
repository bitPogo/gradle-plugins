/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.GradleArtifact
import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.Platform
import tech.antibytes.gradle.dependency.VersionlessGradlePlugin
import tech.antibytes.gradle.dependency.module.android.AppCompact
import tech.antibytes.gradle.dependency.module.android.Coil
import tech.antibytes.gradle.dependency.module.android.Compose
import tech.antibytes.gradle.dependency.module.android.ConstraintLayout
import tech.antibytes.gradle.dependency.module.android.Hilt
import tech.antibytes.gradle.dependency.module.android.Ktx
import tech.antibytes.gradle.dependency.module.android.Material
import tech.antibytes.gradle.dependency.module.android.Material3
import tech.antibytes.gradle.dependency.module.android.Test

internal object Android {
    // AGP
    val agp = GradleArtifact(
        group = "com.android.tools.build",
        id = "gradle",
    )

    val library = VersionlessGradlePlugin("com.android.library")
    val application = VersionlessGradlePlugin("com.android.application")

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
