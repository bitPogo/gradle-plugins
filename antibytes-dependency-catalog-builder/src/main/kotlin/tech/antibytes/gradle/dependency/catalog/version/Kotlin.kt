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
        const val jdk = version
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
    const val android = version
    const val jvm = version
    const val reflect = version
    val parcelize = GradleBundleVersion(version)

    /**
     * [Kotlinx Wrappers](https://github.com/JetBrains/kotlin-wrappers/releases)
     */
    val wrappers = Wrappers

    object Wrappers {
        private const val version = GradleVersions.kotlinWrappers

        const val bom = version
        const val web = version
        const val typescript = version
        const val popper = version
        const val node = version
        const val mui = version
        const val muiIcons = version
        const val js = version
        const val history = version
        const val extensions = version
        const val emotion = version
        const val cssType = version
        const val css = version
        const val cesium = version
        const val browser = version

        val tanstack = Tanstack

        object Tanstack {
            const val virtualCore = version
            const val queryCore = version
            const val tableCore = version

            val react = React

            object React {
                const val query = version
                const val queryDevtools = version
                const val table = version
                const val virtual = version
            }
        }

        const val styledNext = version
        const val ringUi = version
        const val remixRunRouter = version
        const val redux = version

        val react = React

        object React {
            const val main = version
            const val beautifulDnD = version
            const val core = version
            const val dom = version
            const val legacyDom = version
            const val domTestUtils = version
            const val legacy = version
            const val redux = version
            const val router = version
            const val routerDom = version
            const val popper = version
            const val select = version
            const val use = version
        }
    }
}
