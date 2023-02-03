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
import tech.antibytes.gradle.dependency.catalog.module.kotlin.Wrappers

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
    val multiplatform = GradlePlugin(
        id = "org.jetbrains.kotlin.multiplatform",
    )
    val android = GradlePlugin(
        id = "org.jetbrains.kotlin.android",
    )
    val jvm = GradlePlugin(
        id = "org.jetbrains.kotlin.jvm",
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
    val parcelize = GradleBundle(
        group = group,
        id = "kotlin-parcelize-runtime",
        plugin = "kotlin-parcelize",
    )
    val wrappers = Wrappers
}
