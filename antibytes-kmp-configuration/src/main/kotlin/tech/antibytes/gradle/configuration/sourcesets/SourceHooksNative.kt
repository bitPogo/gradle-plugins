/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.sourcesets

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KotlinMultiplatformExtension.native(
    name: String = "native",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    androidNative(configuration = configuration)
    apple(configuration = configuration)
    linux(configuration = configuration)
    mingw(configuration = configuration)

    val nativeMain = sourceSets.maybeCreate("${name}Main")
    val nativeTest = sourceSets.maybeCreate("${name}Test")

    val targets = setOf(
        "androidNative",
        "apple",
        "linux",
        "mingw",
    )

    wireDependencies(
        main = nativeMain,
        test = nativeTest,
        targets = targets,
    )
}

fun KotlinMultiplatformExtension.nativeCoroutine(
    name: String = "native",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    apple(configuration = configuration)
    linuxX64(configure = configuration)
    mingwX64(configure = configuration)

    val nativeMain = sourceSets.maybeCreate("${name}Main")
    val nativeTest = sourceSets.maybeCreate("${name}Test")

    val targets = setOf(
        "apple",
        "linuxX64",
        "mingwX64",
    )

    wireDependencies(
        main = nativeMain,
        test = nativeTest,
        targets = targets,
    )
}
