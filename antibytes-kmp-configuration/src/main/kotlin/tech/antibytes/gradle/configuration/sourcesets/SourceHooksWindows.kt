/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.windows(
    namePrefix: String = "mingw",
    configuration: KotlinNativeTarget.() -> Unit = { }
) {
    mingwX64("${namePrefix}X64", configuration)
    mingwX86("${namePrefix}X86", configuration)

    val windowsMain = sourceSets.create("${namePrefix}Main")
    val windowsTest = sourceSets.create("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}X64",
        "${namePrefix}X86",
    )

    depends(
        targets = targets,
        mainDependency = windowsMain,
        testDependency = windowsTest
    )
}
