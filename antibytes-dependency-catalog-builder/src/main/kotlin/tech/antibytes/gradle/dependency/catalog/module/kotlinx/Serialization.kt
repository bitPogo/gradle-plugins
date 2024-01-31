/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.kotlinx

import tech.antibytes.gradle.dependency.catalog.GradleArtifact
import tech.antibytes.gradle.dependency.catalog.GradlePlugin
import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.Kotlinx

internal object Serialization {
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
        Platform.LINUX_X64,
        Platform.MACOS_ARM64,
        Platform.MACOS_X64,
        Platform.TVOS_ARM64,
        Platform.TVOS_SIMULATOR_ARM64,
        Platform.TVOS_X64,
        Platform.WASM32,
        Platform.WATCHOS_ARM32,
        Platform.WATCHOS_ARM64,
        Platform.WATCHOS_SIMULATOR_ARM64,
        Platform.WATCHOS_X64,
        Platform.WATCHOS_X86,
    )

    val bom = MavenArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-bom",
    )

    val core = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-core",
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
            Platform.LINUX_ARM64,
            Platform.LINUX_ARM32_HFP,
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

    val cbor = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-cbor",
        platforms = allPlatforms,
    )

    val json = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-json",
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
            Platform.LINUX_ARM64,
            Platform.LINUX_ARM32_HFP,
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

    val jsonOkio = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-json-okio",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
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

    val hocon = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-hocon",
        platforms = allPlatforms,
    )

    val properties = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-properties",
        platforms = allPlatforms,
    )

    val protobuf = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "kotlinx-serialization-protobuf",
        platforms = allPlatforms,
    )

    val gradle = GradleArtifact(
        group = "org.jetbrains.kotlin",
        id = "kotlin-serialization",
    )

    val plugin = GradlePlugin(
        id = "org.jetbrains.kotlin.plugin.serialization",
    )
}
