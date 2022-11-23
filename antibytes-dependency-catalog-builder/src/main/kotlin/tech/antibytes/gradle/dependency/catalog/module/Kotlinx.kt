/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.kotlinx.AtomicFu
import tech.antibytes.gradle.dependency.catalog.module.kotlinx.Coroutines
import tech.antibytes.gradle.dependency.catalog.module.kotlinx.Serialization

internal object Kotlinx {
    internal const val group = "org.jetbrains.kotlinx"
    val atomicfu = AtomicFu

    val coroutines = Coroutines

    val dateTime = MavenKmpArtifact(
        group = group,
        id = "kotlinx-datetime",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.LINUX_X64,
            Platform.MACOS_ARM64,
            Platform.MACOS_X64,
            Platform.TVOS_ARM64,
            Platform.TVOS_SIMULATOR_ARM64,
            Platform.TVOS_X64,
            Platform.WATCHOS_ARM32,
            Platform.WATCHOS_ARM64,
            Platform.WATCHOS_SIMULATOR_ARM64,
            Platform.WATCHOS_X64,
            Platform.WATCHOS_X86,
        ),
    )

    val nodeJs = MavenArtifact(
        group = group,
        id = "kotlinx-nodejs",
        platform = Platform.JS,
    )

    val serialization = Serialization
}
