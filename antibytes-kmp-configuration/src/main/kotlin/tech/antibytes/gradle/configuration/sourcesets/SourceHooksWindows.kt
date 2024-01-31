/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.mingw(
    namePrefix: String = "mingw",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    mingwX64("${namePrefix}X64", configuration)
    mingwX86("${namePrefix}X86", configuration)

    val windowsMain = sourceSets.maybeCreate("${namePrefix}Main")
    val windowsTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}X64",
        "${namePrefix}X86",
    )

    wireDependencies(
        main = windowsMain,
        test = windowsTest,
        targets = targets,
    )
}
