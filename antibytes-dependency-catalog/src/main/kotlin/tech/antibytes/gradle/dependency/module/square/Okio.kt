/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.square

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.MavenKmpArtifact
import tech.antibytes.gradle.dependency.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.Platform

internal object Okio {
    private const val group = "com.squareup.okio"
    private val allPlatforms = listOf(
        Platform.COMMON,
        Platform.IOS_ARM64,
        Platform.IOS_X64,
        Platform.IOS_SIMULATOR_ARM64,
        Platform.JS,
        Platform.JVM,
        Platform.LINUX_X64,
        Platform.MACOS_ARM64,
        Platform.MACOS_X64,
        Platform.WINDOWS_X64,
        Platform.TVOS_ARM64,
        Platform.TVOS_SIMULATOR_ARM64,
        Platform.TVOS_X64,
        Platform.WATCHOS_ARM32,
        Platform.WATCHOS_ARM64,
        Platform.WATCHOS_SIMULATOR_ARM64,
        Platform.WATCHOS_X64,
        Platform.WATCHOS_X86,
    )

    val core = MavenKmpArtifact(
        group = group,
        id = "okio",
        platforms = allPlatforms,
    )

    val fakefilesystem = MavenKmpTestArtifact(
        group = group,
        id = "okio-fakefilesystem",
        platforms = allPlatforms,
    )
    val nodefilesystem = MavenArtifact(
        group = group,
        id = "okio-nodefilesystem",
        platform = Platform.JS,
    )
    val bom = MavenArtifact(
        group = group,
        id = "okio-bom",
        platform = Platform.COMMON
    )
}
