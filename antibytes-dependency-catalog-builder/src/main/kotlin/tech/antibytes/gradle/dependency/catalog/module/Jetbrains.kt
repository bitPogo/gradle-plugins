/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.GradleBundle

internal object Jetbrains {
    private const val group = "org.jetbrains.compose"
    val compose = GradleBundle(
        group = group,
        id = "compose-gradle-plugin",
        plugin = "org.jetbrains.compose",
    )
}
