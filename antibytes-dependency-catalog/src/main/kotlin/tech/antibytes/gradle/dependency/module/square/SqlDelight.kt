/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.square

import tech.antibytes.gradle.dependency.GradleArtifact
import tech.antibytes.gradle.dependency.GradlePlugin
import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.MavenKmpArtifact
import tech.antibytes.gradle.dependency.Platform

internal object SqlDelight {
    private const val group = "com.squareup.sqldelight"
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
            id = "sqljs-driver",
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
                Platform.WINDOWS_X64,
                Platform.WATCHOS_X86,
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
            Platform.WINDOWS_X64,
            Platform.WATCHOS_X86,
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
        id = "com.squareup.sqldelight",
    )
}
