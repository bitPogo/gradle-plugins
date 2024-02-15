/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.tvosx(
    namePrefix: String = "tvos",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    tvosArm64("${namePrefix}Arm64", configuration)
    tvosX64("${namePrefix}X64", configuration)
    tvosSimulatorArm64("${namePrefix}SimulatorArm64", configuration)

    val tvosMain = sourceSets.maybeCreate("${namePrefix}Main")
    val tvosTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}X64",
        "${namePrefix}Arm64",
        "${namePrefix}SimulatorArm64",
    )

    wireDependencies(
        main = tvosMain,
        test = tvosTest,
        targets = targets,
    )
}
