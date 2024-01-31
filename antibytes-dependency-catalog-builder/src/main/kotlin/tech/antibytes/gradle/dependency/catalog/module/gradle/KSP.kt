/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.gradle

import tech.antibytes.gradle.dependency.catalog.GradleArtifact
import tech.antibytes.gradle.dependency.catalog.GradleBundle

internal object KSP {
    private const val group = "com.google.devtools.ksp"
    val runtime = GradleArtifact(
        group = group,
        id = "symbol-processing-api",
    )
    val plugin = GradleBundle(
        group = group,
        id = "symbol-processing-gradle-plugin",
        plugin = "com.google.devtools.ksp",
    )
}
