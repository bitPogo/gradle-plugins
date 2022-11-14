/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

import tech.antibytes.gradle.dependency.GradleBundleVersion

internal object Kotlin {
    /**
     * [Kotlin](https://github.com/JetBrains/kotlin)
     */
    private const val version = "1.7.10"
    const val language = version

    const val bom = version

    val stdlib = StdLib

    internal object StdLib {
        const val common = version
        const val jvm = version
        const val jdk7 = version
        const val jdk8 = version
        const val js = version
        const val wasm = version
    }

    val scripting = Scripting

    internal object Scripting {
        const val core = version
        const val jsr223 = version
        const val jsr223Unshaded = version
        const val jvm = version
        const val jvmHostUnshaded = version
        const val common = version
        const val dependencies = version
        const val dependenciesMaven = version
    }

    val test = Test

    internal object Test {
        const val annotations = version
        const val annotationsCommon = version
        const val core = version
        const val common = version
        const val jvm = version
        const val js = version
        const val junit4 = version
        const val junit5 = version
        const val testng = version
        const val wasm = version
    }

    val kotlin = GradleBundleVersion(version)
    const val kmp = version
    const val reflect = version
}
