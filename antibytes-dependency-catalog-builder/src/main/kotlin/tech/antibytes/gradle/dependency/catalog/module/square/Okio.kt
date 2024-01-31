/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.square

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.Square

internal object Okio {
    private const val group = "${Square.domain}.okio"
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
        platform = Platform.COMMON,
    )
}
