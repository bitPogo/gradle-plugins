/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.kotlinx

import tech.antibytes.gradle.dependency.catalog.GradleArtifact
import tech.antibytes.gradle.dependency.catalog.GradlePlugin
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.Platform
import tech.antibytes.gradle.dependency.catalog.module.Kotlinx

internal object AtomicFu {
    val core = MavenKmpArtifact(
        group = Kotlinx.group,
        id = "atomicfu",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.IOS_ARM32,
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
    val gradle = GradleArtifact(
        group = Kotlinx.group,
        id = "atomicfu-gradle-plugin",
    )
    val plugin = GradlePlugin(
        id = "kotlinx-atomicfu",
    )
}
