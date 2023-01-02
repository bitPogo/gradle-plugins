/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.tvosx(
    namePrefix: String = "tvos",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    tvosSimulatorArm64("${namePrefix}SimulatorArm64", configuration)
    tvos(namePrefix, configuration)

    depends(setOf("${namePrefix}SimulatorArm64"), namePrefix)
}
