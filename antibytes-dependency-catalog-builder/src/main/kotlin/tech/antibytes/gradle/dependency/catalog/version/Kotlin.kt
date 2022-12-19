/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.GradleBundleVersion
import tech.antibytes.gradle.dependency.config.GradleVersions

internal object Kotlin {
    /**
     * [Kotlin](https://github.com/JetBrains/kotlin)
     */
    private const val version = GradleVersions.kotlin
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
        val annotations = version

        val core = Core
        internal object Core {
            const val common = version
            const val jvm = version
            const val js = version
            const val wasm = version
        }

        const val junit4 = version
        const val junit5 = version
        const val testng = version
    }

    val kotlin = GradleBundleVersion(version)
    const val multiplatform = version
    const val reflect = version
}
