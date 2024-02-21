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
    iosX64("${namePrefix}X64", configuration)
    iosArm64("${namePrefix}Arm64", configuration)
    iosSimulatorArm64("${namePrefix}SimulatorArm64", configuration)

    val iosMain = sourceSets.maybeCreate("${namePrefix}Main")
    val iosTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}X64",
        "${namePrefix}Arm64",
        "${namePrefix}SimulatorArm64",
    )

    wireDependencies(
        main = iosMain,
        test = iosTest,
        targets = targets,
    )
}
