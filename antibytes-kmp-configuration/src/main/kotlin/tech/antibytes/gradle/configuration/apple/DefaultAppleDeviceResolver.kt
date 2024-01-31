/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.apple

import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests
import tech.antibytes.gradle.configuration.AppleConfigurationApiContract.Companion.IOS_14
import tech.antibytes.gradle.configuration.AppleConfigurationApiContract.Companion.IOS_15
import tech.antibytes.gradle.configuration.AppleConfigurationApiContract.Companion.WATCH_7
import tech.antibytes.gradle.configuration.VersionDescriptor

private val THRESHOLD_IPHONE14 = VersionDescriptor(12, 6, 0)
private val THRESHOLD_IPHONE15 = VersionDescriptor(14, 0, 0)

private fun KotlinMultiplatformExtension.overrideAppleDevice(
    iPhoneVersion: String,
    watchVersion: String,
) {
    val appleTargets = targets.withType(KotlinNativeTargetWithSimulatorTests::class.java)

    appleTargets.forEach { target ->
        when {
            target.name.startsWith("ios") -> {
                target.testRuns["test"].deviceId = iPhoneVersion
            }
            target.name.startsWith("watchos") -> {
                target.testRuns["test"].deviceId = watchVersion
            }
            else -> { /* Do nothing*/ }
        }
    }
}

private fun KotlinMultiplatformExtension.ensureAppleDeviceCompatibility(
    os: OperatingSystem,
    customIos: String = "",
) {
    val version = VersionDescriptor(os.version)

    when {
        customIos.isNotBlank() -> overrideAppleDevice(
            customIos,
            WATCH_7,
        )
        version >= THRESHOLD_IPHONE15 -> overrideAppleDevice(
            IOS_15,
            WATCH_7,
        )
        version >= THRESHOLD_IPHONE14 -> overrideAppleDevice(
            IOS_14,
            WATCH_7,
        )
    }
}

// see https://youtrack.jetbrains.com/issue
// /KT-45416/Do-not-use-iPhone-8-simulator-for-Gradle-tests
fun KotlinMultiplatformExtension.ensureAppleDeviceCompatibility(
    iosDevice: String = "",
) {
    val os = OperatingSystem.current()

    if (os.isMacOsX) {
        ensureAppleDeviceCompatibility(os, iosDevice)
    }
}
