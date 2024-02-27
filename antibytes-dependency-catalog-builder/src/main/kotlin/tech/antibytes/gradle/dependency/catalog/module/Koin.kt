/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.catalog.MavenTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

/**
 * [Koin](https://github.com/InsertKoinIO/koin)
 */
internal object Koin {
    private const val group = "io.insert-koin"

    val annotations = MavenKmpArtifact(
        group = group,
        id = "koin-annotations",
        platforms = listOf(
            Platform.COMMON,
            Platform.JVM,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.WINDOWS_X86,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
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

    val core = MavenKmpArtifact(
        group = group,
        id = "koin-core",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.WINDOWS_X86,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
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
    val ktor = MavenArtifact(
        group = group,
        id = "koin-ktor",
    )
    val slf4j = MavenArtifact(
        group = group,
        id = "koin-slf4j",
    )
    val test = MavenKmpTestArtifact(
        group = group,
        id = "koin-test",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.WINDOWS_X86,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
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
    val junit4 = MavenTestArtifact(
        group = group,
        id = "koin-test-junit4",
    )
    val junit5 = MavenTestArtifact(
        group = group,
        id = "koin-test-junit5",
    )
    val androidBinding = MavenArtifact(
        group = group,
        id = "koin-android",
        platform = Platform.ANDROID,
    )
    val appCompat = MavenArtifact(
        group = group,
        id = "koin-android-compat",
        platform = Platform.ANDROID,
    )
    val compose = MavenArtifact(
        group = group,
        id = "koin-androidx-compose",
        platform = Platform.ANDROID,
    )
    val androidTest = MavenTestArtifact(
        group = group,
        id = "koin-android-test",
        platform = Platform.ANDROID,
    )
}
