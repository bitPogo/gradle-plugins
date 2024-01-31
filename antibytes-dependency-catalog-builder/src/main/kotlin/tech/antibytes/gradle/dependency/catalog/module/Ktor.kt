/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.GradleBundle
import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.ktor.Client
import tech.antibytes.gradle.dependency.catalog.module.ktor.Serialization
import tech.antibytes.gradle.dependency.catalog.module.ktor.Server

internal object Ktor {
    internal const val group = "io.ktor"
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

    val client = Client

    val events = MavenKmpArtifact(
        group = group,
        id = "ktor-events",
        platforms = allPlatforms,
    )

    val http = MavenKmpArtifact(
        group = group,
        id = "ktor-http",
        platforms = allPlatforms,
    )

    val httpCio = MavenKmpArtifact(
        group = group,
        id = "ktor-http-cio",
        platforms = allPlatforms,
    )

    val io = MavenKmpArtifact(
        group = group,
        id = "ktor-io",
        platforms = allPlatforms,
    )

    val network = MavenKmpArtifact(
        group = group,
        id = "ktor-network",
        platforms = listOf(
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

    val networkTls = MavenKmpArtifact(
        group = group,
        id = "ktor-network-tls",
        platforms = listOf(
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

    val networkTlsCertificates = MavenArtifact(
        group = group,
        id = "ktor-network-tls-certificates",
    )

    val serialization = Serialization

    val server = Server

    val utils = MavenKmpArtifact(
        group = group,
        id = "ktor-utils",
        platforms = allPlatforms,
    )

    val websocketSerialization = MavenKmpArtifact(
        group = group,
        id = "ktor-websocket-serialization",
        platforms = allPlatforms,
    )

    val websockets = MavenKmpArtifact(
        group = group,
        id = "ktor-websockets",
        platforms = allPlatforms,
    )

    val plugin = GradleBundle(
        group = "io.ktor.plugin",
        id = "io.ktor.plugin",
        plugin = "io.ktor.plugin.gradle.plugin",
    )
}
