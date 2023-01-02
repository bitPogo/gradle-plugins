/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.macos(
    namePrefix: String = "macos",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    macosArm64("${namePrefix}Arm64", configuration)
    macosX64("${namePrefix}X64", configuration)

    val macosMain = sourceSets.create("${namePrefix}Main")
    val macosTest = sourceSets.create("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}Arm64",
        "${namePrefix}X64",
    )

    depends(
        targets = targets,
        mainDependency = macosMain,
        testDependency = macosTest,
    )
}
