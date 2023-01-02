/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.watchosx(
    namePrefix: String = "watchos",
    configuration: KotlinNativeTarget.() -> Unit = { }
) {
    watchosSimulatorArm64("${namePrefix}SimulatorArm64", configuration)
    watchos(namePrefix, configuration)

    depends(setOf("${namePrefix}SimulatorArm64"), namePrefix)
}

private fun KotlinMultiplatformExtension.legacyWatchos(
    namePrefix: String,
    configuration: KotlinNativeTarget.() -> Unit,
    provider: KotlinMultiplatformExtension.() -> Unit,
) {
    watchosX86("${namePrefix}X86", configuration)
    provider()

    depends(setOf("${namePrefix}X86"), namePrefix)
}

fun KotlinMultiplatformExtension.watchosWithLegacy(
    namePrefix: String = "watchos",
    configuration: KotlinNativeTarget.() -> Unit = { }
) = legacyWatchos(namePrefix, configuration) { watchos(namePrefix, configuration) }

fun KotlinMultiplatformExtension.watchosxWithLegacy(
    namePrefix: String = "watchos",
    configuration: KotlinNativeTarget.() -> Unit = { }
) = legacyWatchos(namePrefix, configuration) { watchosx(namePrefix, configuration) }
