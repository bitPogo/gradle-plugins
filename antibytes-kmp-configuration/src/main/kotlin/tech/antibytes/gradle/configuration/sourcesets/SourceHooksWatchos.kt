/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.watchosx(
    namePrefix: String = "watchos",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    watchosArm32("${namePrefix}Arm32", configuration)
    watchosArm64("${namePrefix}Arm64", configuration)
    watchosX64("${namePrefix}X64", configuration)
    watchosSimulatorArm64("${namePrefix}SimulatorArm64", configuration)

    val watchosMain = sourceSets.maybeCreate("${namePrefix}Main")
    val watchosTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}Arm32",
        "${namePrefix}Arm64",
        "${namePrefix}X64",
        "${namePrefix}SimulatorArm64",
    )

    wireDependencies(
        main = watchosMain,
        test = watchosTest,
        targets = targets,
    )
}
