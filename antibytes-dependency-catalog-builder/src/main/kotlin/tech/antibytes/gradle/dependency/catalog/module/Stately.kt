/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.Platform

/**
 * [Stately](https://github.com/touchlab/Stately)
 */
internal object Stately {
    private const val group = "co.touchlab"

    val isolate = MavenKmpArtifact(
        group = group,
        id = "stately-isolate",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.ANDROID_NATIVE_ARM32,
            Platform.ANDROID_NATIVE_ARM64,
            Platform.ANDROID_NATIVE_X64,
            Platform.ANDROID_NATIVE_X86,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.WINDOWS_X86,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
            Platform.LINUX_MIPS32,
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
    val freeze = MavenKmpArtifact(
        group = group,
        id = "stately-common",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.ANDROID_NATIVE_ARM32,
            Platform.ANDROID_NATIVE_ARM64,
            Platform.ANDROID_NATIVE_X64,
            Platform.ANDROID_NATIVE_X86,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.WINDOWS_X86,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
            Platform.LINUX_MIPS32,
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
    val concurrency = MavenKmpArtifact(
        group = group,
        id = "stately-concurrency",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.ANDROID_NATIVE_ARM32,
            Platform.ANDROID_NATIVE_ARM64,
            Platform.ANDROID_NATIVE_X64,
            Platform.ANDROID_NATIVE_X86,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.WINDOWS_X86,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
            Platform.LINUX_MIPS32,
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
    val collections = MavenKmpArtifact(
        group = group,
        id = "stately-iso-collections",
        platforms = listOf(
            Platform.COMMON,
            Platform.JS,
            Platform.JVM,
            Platform.ANDROID_NATIVE_ARM32,
            Platform.ANDROID_NATIVE_ARM64,
            Platform.ANDROID_NATIVE_X64,
            Platform.ANDROID_NATIVE_X86,
            Platform.IOS_ARM32,
            Platform.IOS_ARM64,
            Platform.IOS_SIMULATOR_ARM64,
            Platform.IOS_X64,
            Platform.WINDOWS_X64,
            Platform.WINDOWS_X86,
            Platform.LINUX_X64,
            Platform.LINUX_ARM32_HFP,
            Platform.LINUX_MIPS32,
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
