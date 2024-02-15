/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

private fun KotlinMultiplatformExtension.createDevice(
    namePrefix: String = "watchos",
    dependencies: Set<KotlinNativeTarget>,
) {
    val deviceMain = sourceSets.maybeCreate("${namePrefix}DeviceMain")
    val deviceTest = sourceSets.maybeCreate("${namePrefix}DeviceTest")

    dependencies.forEach { sourceSet ->
        deviceMain.dependsOn(sourceSet.compilations.getByName("main").defaultSourceSet)
        deviceTest.dependsOn(sourceSet.compilations.getByName("test").defaultSourceSet)
    }
}

fun KotlinMultiplatformExtension.watchosx(
    namePrefix: String = "watchos",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    val device32 = watchosArm32("${namePrefix}Arm32", configuration)
    val device64 = watchosArm64("${namePrefix}Arm64", configuration)
    watchosX64("${namePrefix}X64", configuration)
    watchosSimulatorArm64("${namePrefix}SimulatorArm64", configuration)
    createDevice(namePrefix, setOf(device32, device64))

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
