/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.ktor

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.MavenKmpArtifact
import tech.antibytes.gradle.dependency.Platform
import tech.antibytes.gradle.dependency.module.Ktor

internal object Serialization {
    val cbor = MavenKmpArtifact(
        group = Ktor.group,
        id = "ktor-serialization-kotlinx-cbor",
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

    val core = MavenKmpArtifact(
        group = Ktor.group,
        id = "ktor-serialization-kotlinx",
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

    val gson = MavenArtifact(
        group = Ktor.group,
        id = "ktor-serialization-kotlinx-gson",
    )

    val jackson = MavenArtifact(
        group = Ktor.group,
        id = "ktor-serialization-kotlinx-jackson",
    )

    val json = MavenArtifact(
        group = Ktor.group,
        id = "ktor-serialization-kotlinx-json",
    )

    val xml = MavenArtifact(
        group = Ktor.group,
        id = "ktor-serialization-kotlinx-xml",
    )
}
