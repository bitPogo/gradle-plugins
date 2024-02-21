/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.linux(
    namePrefix: String = "linux",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    linuxX64("${namePrefix}X64", configuration)
    linuxArm64("${namePrefix}Arm64", configuration)

    val linuxMain = sourceSets.maybeCreate("${namePrefix}Main")
    val linuxTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}X64",
        "${namePrefix}Arm64",
    )

    wireDependencies(
        main = linuxMain,
        test = linuxTest,
        targets = targets,
    )
}
