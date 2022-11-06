/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.ktor

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.MavenKmpArtifact
import tech.antibytes.gradle.dependency.Platform
import tech.antibytes.gradle.dependency.module.Ktor.group

internal object Client {
    private val allPlatforms = listOf(
        Platform.COMMON,
        Platform.IOS_ARM32,
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

    val android = MavenArtifact(
        group = group,
        id = "ktor-client-android",
        type = Platform.ANDROID,
    )

    val apache = MavenArtifact(
        group = group,
        id = "ktor-client-apache",
    )

    val auth = MavenKmpArtifact(
        group = group,
        id = "ktor-client-auth",
        platforms = allPlatforms,
    )

    val cio = MavenKmpArtifact(
        group = group,
        id = "ktor-client-cio",
        platforms = listOf(
            Platform.COMMON,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_X64,
            Platform.IOS_SIMULATOR_ARM64,
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
        ),
    )

    val contentNegotiation = MavenKmpArtifact(
        group = group,
        id = "ktor-client-content-negotiation",
        platforms = allPlatforms,
    )

    val core = MavenKmpArtifact(
        group = group,
        id = "ktor-client-core",
        platforms = allPlatforms,
    )

    val curl = MavenKmpArtifact(
        group = group,
        id = "ktor-client-curl",
        platforms = listOf(
            Platform.COMMON,
            Platform.LINUX_X64,
            Platform.MACOS_ARM64,
            Platform.WINDOWS_X64,
        ),
    )

    val darwin = MavenKmpArtifact(
        group = group,
        id = "ktor-client-darwin",
        platforms = listOf(
            Platform.COMMON,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_X64,
            Platform.IOS_SIMULATOR_ARM64,
        ),
    )

    val darwinLegacy = MavenKmpArtifact(
        group = group,
        id = "ktor-client-darwin-legacy",
        platforms = listOf(
            Platform.COMMON,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_X64,
            Platform.IOS_SIMULATOR_ARM64,
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

    val encoding = MavenKmpArtifact(
        group = group,
        id = "ktor-client-encoding",
        platforms = allPlatforms,
    )

    val gson = MavenArtifact(
        group = group,
        id = "ktor-client-gson",
    )

    val ios = MavenKmpArtifact(
        group = group,
        id = "ktor-client-ios",
        platforms = listOf(
            Platform.COMMON,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_X64,
            Platform.IOS_SIMULATOR_ARM64,
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

    val jackson = MavenArtifact(
        group = group,
        id = "ktor-client-jackson",
    )

    val java = MavenArtifact(
        group = group,
        id = "ktor-client-java",
    )

    val jetty = MavenArtifact(
        group = group,
        id = "ktor-client-jetty",
    )

    val json = MavenKmpArtifact(
        group = group,
        id = "ktor-client-encoding",
        platforms = allPlatforms,
    )

    val logging = MavenKmpArtifact(
        group = group,
        id = "ktor-client-logging",
        platforms = allPlatforms,
    )

    val okhttp = MavenArtifact(
        group = group,
        id = "ktor-client-okhttp",
    )

    val resources = MavenKmpArtifact(
        group = group,
        id = "ktor-client-resources",
        platforms = allPlatforms,
    )

    val serialisation = MavenKmpArtifact(
        group = group,
        id = "ktor-client-serialisation",
        platforms = allPlatforms,
    )

    val websockets = MavenKmpArtifact(
        group = group,
        id = "ktor-client-websockets",
        platforms = allPlatforms,
    )
}
