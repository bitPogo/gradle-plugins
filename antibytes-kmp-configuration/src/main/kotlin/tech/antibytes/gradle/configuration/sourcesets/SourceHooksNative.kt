/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
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
    wasm32(configure = configuration)
    windows(configuration = configuration)

    val nativeMain = sourceSets.maybeCreate("${name}Main")
    val nativeTest = sourceSets.maybeCreate("${name}Test")

    val targets = setOf(
        "androidNative",
        "apple",
        "linux",
        "wasm32",
        "windows",
    )

    wireDependencies(
        main = nativeMain,
        test = nativeTest,
        targets = targets,
    )
}

fun KotlinMultiplatformExtension.nativeWithLegacy(
    name: String = "native",
    configuration: KotlinNativeTarget.() -> Unit = { },
) {
    androidNative(configuration = configuration)
    appleWithLegacy(configuration = configuration)
    linux(configuration = configuration)
    wasm32(configure = configuration)
    windows(configuration = configuration)

    val nativeMain = sourceSets.maybeCreate("${name}Main")
    val nativeTest = sourceSets.maybeCreate("${name}Test")

    val targets = setOf(
        "androidNative",
        "apple",
        "linux",
        "wasm32",
        "windows",
    )

    wireDependencies(
        main = nativeMain,
        test = nativeTest,
        targets = targets,
    )
}
