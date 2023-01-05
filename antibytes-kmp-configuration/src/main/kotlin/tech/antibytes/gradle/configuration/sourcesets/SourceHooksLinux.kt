/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.linuxArm(
    namePrefix: String = "linuxArm",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    linuxArm64("${namePrefix}64", configuration)
    linuxArm32Hfp("${namePrefix}32Hfp", configuration)

    val linuxMain = sourceSets.maybeCreate("${namePrefix}Main")
    val linuxTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}64",
        "${namePrefix}32Hfp",
    )

    wireDependencies(
        main = linuxMain,
        test = linuxTest,
        targets = targets,
    )
}

fun KotlinMultiplatformExtension.linuxMips(
    namePrefix: String = "linuxMips",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    linuxMips32("${namePrefix}32", configuration)
    linuxMipsel32("${namePrefix}el32", configuration)

    val linuxMain = sourceSets.maybeCreate("${namePrefix}Main")
    val linuxTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}32",
        "${namePrefix}el32",
    )

    wireDependencies(
        main = linuxMain,
        test = linuxTest,
        targets = targets,
    )
}

fun KotlinMultiplatformExtension.linux(
    namePrefix: String = "linux",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    linuxX64("${namePrefix}X64", configuration)
    linuxMips("${namePrefix}Mips", configuration)
    linuxArm("${namePrefix}Arm", configuration)

    val linuxMain = sourceSets.maybeCreate("${namePrefix}Main")
    val linuxTest = sourceSets.maybeCreate("${namePrefix}Test")

    val targets = setOf(
        "${namePrefix}X64",
        "${namePrefix}Arm",
        "${namePrefix}Mips",
    )

    wireDependencies(
        main = linuxMain,
        test = linuxTest,
        targets = targets,
    )
}
