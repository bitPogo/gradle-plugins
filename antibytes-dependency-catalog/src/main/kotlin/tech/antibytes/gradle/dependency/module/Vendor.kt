/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.MavenKmpArtifact
import tech.antibytes.gradle.dependency.MavenKmpTestArtifact
import tech.antibytes.gradle.dependency.Platform
import tech.antibytes.gradle.dependency.module.SLF4J

internal object Vendor {
    val mockk = MavenKmpTestArtifact(
        group = "io.mockk",
        id = "mockk",
        platforms = listOf(
            Platform.COMMON,
            Platform.ANDROID,
            Platform.JVM,
        )
    )

    val slf4j = SLF4J

    val uuid = MavenKmpArtifact(
        group = "com.benasher44",
        id = "uuid",
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
            Platform.LINUX_ARM32_HFP,
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
}
