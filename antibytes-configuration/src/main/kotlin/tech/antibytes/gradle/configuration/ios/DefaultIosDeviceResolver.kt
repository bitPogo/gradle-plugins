/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import tech.antibytes.gradle.configuration.IosConfigurationApiContract.Companion.IOS_DEFAULT_DEVICE

private val THRESHOLD = VersionDescriptor(12, 6, 0)

private fun KotlinMultiplatformExtension.overrideIosDevice() {
    val iosTargets = targets.withType(KotlinNativeTargetWithSimulatorTests::class.java)

    iosTargets.forEach { target ->
        target.testRuns["test"].deviceId = IOS_DEFAULT_DEVICE
    }
}

private fun KotlinMultiplatformExtension.ensureIosDeviceCompatibility(os: OperatingSystem) {
    val version = VersionDescriptor(os.version)

    if (version >= THRESHOLD) {
        overrideIosDevice()
    }
}

// see https://youtrack.jetbrains.com/issue/KT-45416/Do-not-use-iPhone-8-simulator-for-Gradle-tests
fun KotlinMultiplatformExtension.ensureIosDeviceCompatibility() {
    val os = OperatingSystem.current()

    if (os.isMacOsX) {
        ensureIosDeviceCompatibility(os)
    }
}
