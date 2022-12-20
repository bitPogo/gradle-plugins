/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.apple

import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import tech.antibytes.gradle.configuration.AppleConfigurationApiContract.Companion.IOS_DEFAULT_DEVICE
import tech.antibytes.gradle.configuration.AppleConfigurationApiContract.Companion.WATCH_DEFAULT_DEVICE
import tech.antibytes.gradle.configuration.VersionDescriptor

private val THRESHOLD = VersionDescriptor(12, 6, 0)

private fun KotlinMultiplatformExtension.overrideAppleDevice() {
    val appleTargets = targets.withType(KotlinNativeTargetWithSimulatorTests::class.java)

    appleTargets.forEach { target ->
        when {
            target.name.startsWith("ios") -> {
                target.testRuns["test"].deviceId = IOS_DEFAULT_DEVICE
            }
            target.name.startsWith("watchos") -> {
                target.testRuns["test"].deviceId = WATCH_DEFAULT_DEVICE
            }
            else -> { /* Do nothing*/ }
        }
    }
}

private fun KotlinMultiplatformExtension.ensureAppleDeviceCompatibility(os: OperatingSystem) {
    val version = VersionDescriptor(os.version)

    if (version >= THRESHOLD) {
        overrideAppleDevice()
    }
}

// see https://youtrack.jetbrains.com/issue
// /KT-45416/Do-not-use-iPhone-8-simulator-for-Gradle-tests
fun KotlinMultiplatformExtension.ensureAppleDeviceCompatibility() {
    val os = OperatingSystem.current()

    if (os.isMacOsX) {
        ensureAppleDeviceCompatibility(os)
    }
}
