/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.kotlinx

import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.Kotlinx.group

internal object Coroutines {
    private val allPlatforms = listOf(
        Platform.COMMON,
        Platform.JS,
        Platform.JVM,
        Platform.IOS_ARM32,
        Platform.IOS_ARM64,
        Platform.IOS_SIMULATOR_ARM64,
        Platform.IOS_X64,
        Platform.WINDOWS_X64,
        Platform.WINDOWS_X86,
        Platform.LINUX_ARM64,
        Platform.LINUX_ARM32_HFP,
        Platform.LINUX_MIPS32,
        Platform.LINUX_MIPSEL32,
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
    )

    val android = MavenArtifact(
        group = group,
        id = "kotlinx-coroutines-android",
        platform = Platform.ANDROID,
    )

    val bom = MavenArtifact(
        group = group,
        id = "kotlinx-coroutines-bom",
    )

    val core = MavenKmpArtifact(
        group = group,
        id = "kotlinx-coroutines-core",
        platforms = allPlatforms,
    )

    val javafx = MavenArtifact(
        group = group,
        id = "kotlinx-coroutines-javafx",
    )

    val jdk8 = MavenArtifact(
        group = group,
        id = "kotlinx-coroutines-jdk8",
    )

    val jdk9 = MavenArtifact(
        group = group,
        id = "kotlinx-coroutines-jdk9",
    )

    val test = MavenKmpTestArtifact(
        group = group,
        id = "kotlinx-coroutines-test",
        platforms = allPlatforms,
    )
}
