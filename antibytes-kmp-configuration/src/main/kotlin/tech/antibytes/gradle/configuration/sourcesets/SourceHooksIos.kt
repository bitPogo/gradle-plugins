/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.iosx(
    namePrefix: String = "ios",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    ios(namePrefix, configuration)
    iosSimulatorArm64("${namePrefix}SimulatorArm64", configuration)

    depends(setOf("${namePrefix}SimulatorArm64"), namePrefix)
}

private fun KotlinMultiplatformExtension.legacyIos(
    namePrefix: String,
    configuration: KotlinNativeTarget.() -> Unit,
    provider: KotlinMultiplatformExtension.() -> Unit,
) {
    iosArm32("${namePrefix}Arm32", configuration)
    provider()

    depends(setOf("${namePrefix}Arm32"), namePrefix)
}

fun KotlinMultiplatformExtension.iosWithLegacy(
    namePrefix: String = "ios",
    configuration: KotlinNativeTarget.() -> Unit = { },
) = legacyIos(namePrefix, configuration) { ios(namePrefix, configuration) }

fun KotlinMultiplatformExtension.iosxWithLegacy(
    namePrefix: String = "ios",
    configuration: KotlinNativeTarget.() -> Unit = { },
) = legacyIos(namePrefix, configuration) { iosx(namePrefix, configuration) }
