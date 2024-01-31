/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.androidNativeArm(
    namePrefix: String = "androidNativeArm",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    androidNativeArm32("${namePrefix}32", configuration)
    androidNativeArm64("${namePrefix}64", configuration)

    val androidMain = sourceSets.maybeCreate("${namePrefix}Main")
    val androidTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}32",
        "${namePrefix}64",
    )

    wireDependencies(
        main = androidMain,
        test = androidTest,
        targets = targets,
    )
}

fun KotlinMultiplatformExtension.androidNativeX(
    namePrefix: String = "androidNativeX",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    androidNativeX64("${namePrefix}64", configuration)
    androidNativeX86("${namePrefix}86", configuration)

    val androidMain = sourceSets.maybeCreate("${namePrefix}Main")
    val androidTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}64",
        "${namePrefix}86",
    )

    wireDependencies(
        main = androidMain,
        test = androidTest,
        targets = targets,
    )
}

fun KotlinMultiplatformExtension.androidNative(
    namePrefix: String = "androidNative",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    androidNativeArm("${namePrefix}Arm", configuration)
    androidNativeX("${namePrefix}X", configuration)

    val androidMain = sourceSets.maybeCreate("${namePrefix}Main")
    val androidTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}Arm",
        "${namePrefix}X",
    )

    wireDependencies(
        main = androidMain,
        test = androidTest,
        targets = targets,
    )
}
