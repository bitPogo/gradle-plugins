/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.GradleBundle
import tech.antibytes.gradle.dependency.catalog.GradlePlugin
import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.module.kotlin.Scripting
import tech.antibytes.gradle.dependency.catalog.module.kotlin.StdLib
import tech.antibytes.gradle.dependency.catalog.module.kotlin.Test

internal object Kotlin {
    /**
     * [Kotlin](https://github.com/JetBrains/kotlin)
     */
    internal const val group = "org.jetbrains.kotlin"

    val stdlib = StdLib
    val scripting = Scripting
    val test = Test

    val bom = MavenArtifact(
        group = group,
        id = "kotlin-bom",
    )
    val kmp = GradlePlugin(
        id = "org.jetbrains.kotlin.multiplatform",
    )
    val reflect = MavenArtifact(
        group = group,
        id = "kotlin-reflect",
    )
    val kotlin = GradleBundle(
        group = group,
        id = "kotlin-gradle-plugin",
        plugin = "org.gradle.kotlin.kotlin-dsl",
    )
}
