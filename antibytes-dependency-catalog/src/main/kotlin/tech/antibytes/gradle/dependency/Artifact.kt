/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

internal sealed class Artifact {
    open val id: String = ""
}

internal enum class Platform(val platform: String) {
    COMMON("common"),
    ANDROID("android"),
    ANDROID_NATIVE_ARM32("androidNativeArm32"),
    ANDROID_NATIVE_ARM64("androidNativeArm64"),
    ANDROID_NATIVE_X64("androidNativeX64"),
    ANDROID_NATIVE_X86("androidNativeX86"),
    JS("js"),
    JVM("jvm"),
    IOS_ARM32("iosArm32"),
    IOS_ARM64("iosArm64"),
    IOS_SIMULATOR_ARM64("iosSimulatorArm64"),
    IOS_X64("iosX64"),
    LINUX_ARM64("linuxArm64"),
    LINUX_ARM32_HFP("linuxArm32Hfp"),
    LINUX_MIPS32("linuxMips32"),
    LINUX_MIPSEL32("linuxMipsel32"),
    LINUX_X64("linuxX64"),
    MACOS_ARM64("macosArm64"),
    MACOS_X64("macosX64"),
    TVOS_ARM64("tvosArm64"),
    TVOS_SIMULATOR_ARM64("tvosSimulatorArm64"),
    TVOS_X64("tvosX64"),
    WASM32("wasm32"),
    WATCHOS_ARM32("watchosArm32"),
    WATCHOS_ARM64("watchosArm64"),
    WATCHOS_SIMULATOR_ARM64("watchosSimulatorArm64"),
    WATCHOS_X64("watchosX64"),
    WATCHOS_X86("watchosX86"),
    WINDOWS_X64("mingwX64"),
    WINDOWS_X86("mingwX86"),
}

internal data class MavenArtifact(
    val group: String,
    override val id: String,
    val type: Platform = Platform.JVM,
) : Artifact()

internal data class MavenVersionlessArtifact(
    val group: String,
    override val id: String,
) : Artifact()

internal data class MavenKmpArtifact(
    val group: String,
    override val id: String,
    val platforms: List<Platform>,
) : Artifact()

internal data class NodeArtifact(
    override val id: String,
) : Artifact()

internal data class PythonArtifact(
    override val id: String,
) : Artifact()
