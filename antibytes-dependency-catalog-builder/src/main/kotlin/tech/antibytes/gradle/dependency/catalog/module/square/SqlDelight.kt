/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.square

import tech.antibytes.gradle.dependency.catalog.GradleArtifact
import tech.antibytes.gradle.dependency.catalog.GradlePlugin
import tech.antibytes.gradle.dependency.catalog.MavenArtifact
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

internal object SqlDelight {
    private const val group = "app.cash.sqldelight"
    val driver = Driver

    internal object Driver {
        val android = MavenArtifact(
            group = group,
            id = "android-driver",
            platform = Platform.ANDROID,
        )
        val jdbc = MavenArtifact(
            group = group,
            id = "jdbc-driver",
        )
        val jvm = MavenArtifact(
            group = group,
            id = "sqlite-driver",
        )
        val js = MavenArtifact(
            group = group,
            id = "web-worker-driver",
            platform = Platform.JS,
        )
        val native = MavenKmpArtifact(
            group = group,
            id = "native-driver",
            platforms = listOf(
                Platform.COMMON,
                Platform.IOS_X64,
                Platform.IOS_ARM32,
                Platform.IOS_SIMULATOR_ARM64,
                Platform.IOS_ARM64,
                Platform.LINUX_X64,
                Platform.MACOS_ARM64,
                Platform.MACOS_X64,
                Platform.TVOS_ARM64,
                Platform.TVOS_X64,
                Platform.TVOS_SIMULATOR_ARM64,
                Platform.WATCHOS_ARM32,
                Platform.WATCHOS_ARM64,
                Platform.WATCHOS_SIMULATOR_ARM64,
                Platform.WATCHOS_X86,
                Platform.WATCHOS_X64,
            ),
        )
    }

    val coroutines = MavenKmpArtifact(
        group = group,
        id = "coroutines-extensions",
        platforms = listOf(
            Platform.COMMON,
            Platform.JVM,
            Platform.JS,
            Platform.IOS_X64,
            Platform.IOS_ARM32,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_ARM64,
            Platform.LINUX_X64,
            Platform.MACOS_ARM64,
            Platform.MACOS_X64,
            Platform.TVOS_ARM64,
            Platform.TVOS_X64,
            Platform.TVOS_SIMULATOR_ARM64,
            Platform.WATCHOS_ARM32,
            Platform.WATCHOS_ARM64,
            Platform.WATCHOS_SIMULATOR_ARM64,
            Platform.WATCHOS_X86,
            Platform.WATCHOS_X64,
        ),
    )

    val gradle = GradleArtifact(
        group = group,
        id = "gradle-plugin",
    )

    val plugin = GradlePlugin(
        id = "app.cash.sqldelight",
    )
}
