/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

internal interface TestArtifact

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

internal interface SinglePlatformArtifact {
    val group: String
    val id: String
    val platform: Platform
}

internal data class MavenArtifact(
    override val group: String,
    override val id: String,
    override val platform: Platform = Platform.JVM,
) : Artifact(), SinglePlatformArtifact

internal data class MavenTestArtifact(
    override val group: String,
    override val id: String,
    override val platform: Platform = Platform.JVM,
) : Artifact(), SinglePlatformArtifact, TestArtifact

internal interface VersionlessArtifact {
    val group: String
    val id: String
    val platform: Platform
}

internal data class MavenVersionlessArtifact(
    override val group: String,
    override val id: String,
    override val platform: Platform = Platform.JVM,
) : Artifact(), VersionlessArtifact

internal data class MavenVersionlessTestArtifact(
    override val group: String,
    override val id: String,
    override val platform: Platform = Platform.JVM,
) : Artifact(), VersionlessArtifact, TestArtifact

internal interface KmpArtifact {
    val group: String
    val id: String
    val platforms: List<Platform>
}

internal data class MavenKmpArtifact(
    override val group: String,
    override val id: String,
    override val platforms: List<Platform>,
) : Artifact(), KmpArtifact

internal data class MavenKmpTestArtifact(
    override val group: String,
    override val id: String,
    override val platforms: List<Platform>,
) : Artifact(), KmpArtifact, TestArtifact

internal interface Plugin {
    val id: String
}

internal data class GradlePlugin(
    override val id: String,
) : Artifact(), Plugin

internal data class VersionlessGradlePlugin(
    override val id: String,
) : Artifact(), Plugin

internal interface GradleRelatedArtifact {
    val group: String
    val id: String
}

internal data class GradleArtifact(
    override val group: String,
    override val id: String,
) : Artifact(), GradleRelatedArtifact

internal data class GradleTestArtifact(
    override val group: String,
    override val id: String,
) : Artifact(), TestArtifact, GradleRelatedArtifact

internal data class NodeArtifact(
    override val id: String,
) : Artifact()

internal data class PythonArtifact(
    override val id: String,
) : Artifact()

internal class GradleBundleVersion(version: String) {
    val dependency = version
    val plugin = version
}

internal interface Bundle {
    val dependency: GradleRelatedArtifact
    val plugin: GradlePlugin
}

internal class GradleBundle(
    group: String,
    id: String,
    plugin: String,
) {
    val dependency = GradleArtifact(
        group = group,
        id = id,
    )

    val plugin = GradlePlugin(plugin)
}

internal class GradleTestBundle(
    group: String,
    id: String,
    plugin: String,
) {
    val dependency = GradleTestArtifact(
        group = group,
        id = id,
    )

    val plugin = GradlePlugin(plugin)
}
