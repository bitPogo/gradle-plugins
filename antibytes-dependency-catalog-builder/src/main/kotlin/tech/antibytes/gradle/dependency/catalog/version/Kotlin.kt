/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.GradleBundleVersion
import tech.antibytes.gradle.dependency.catalog.MavenKmpArtifact
import tech.antibytes.gradle.dependency.catalog.module.Kotlinx
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

    /**
     * [Kotlinx Wrappers](https://github.com/JetBrains/kotlin-wrappers/releases)
     */
    val wrappers = Wrappers

    object Wrappers {
        const val bom = GradleVersions.kotlinWrappers
        const val web = GradleVersions.kotlinWrappers
        const val typescript = GradleVersions.kotlinWrappers
        const val popper = GradleVersions.kotlinWrappers
        const val node = GradleVersions.kotlinWrappers
        const val mui = GradleVersions.kotlinWrappers
        const val muiIcon = GradleVersions.kotlinWrappers
        const val js = GradleVersions.kotlinWrappers
        const val history = GradleVersions.kotlinWrappers
        const val extensions = GradleVersions.kotlinWrappers
        const val emotion = GradleVersions.kotlinWrappers
        const val cssType = GradleVersions.kotlinWrappers
        const val css = GradleVersions.kotlinWrappers
        const val cesium = GradleVersions.kotlinWrappers
        const val browser = GradleVersions.kotlinWrappers

        val tanstack = Tanstack

        object Tanstack {
            const val virtualCore = GradleVersions.kotlinWrappers
            const val queryCore = GradleVersions.kotlinWrappers

            val react = React

            object React {
                const val query = GradleVersions.kotlinWrappers
                const val queryDevtools = GradleVersions.kotlinWrappers
                const val table = GradleVersions.kotlinWrappers
                const val virtual = GradleVersions.kotlinWrappers
            }
        }

        const val styledNext = GradleVersions.kotlinWrappers
        const val ringUi = GradleVersions.kotlinWrappers
        const val remixRunRouter = GradleVersions.kotlinWrappers
        const val redux = GradleVersions.kotlinWrappers

        val react = React

        object React {
            const val main = GradleVersions.kotlinWrappers
            const val beautifulDnD = GradleVersions.kotlinWrappers
            const val core = GradleVersions.kotlinWrappers
            const val dom = GradleVersions.kotlinWrappers
            const val legacyDom = GradleVersions.kotlinWrappers
            const val domTestUtils = GradleVersions.kotlinWrappers
            const val legacy = GradleVersions.kotlinWrappers
            const val redux = GradleVersions.kotlinWrappers
            const val router = GradleVersions.kotlinWrappers
            const val routerDom = GradleVersions.kotlinWrappers
            const val popper = GradleVersions.kotlinWrappers
            const val select = GradleVersions.kotlinWrappers
            const val use = GradleVersions.kotlinWrappers
        }
    }
}
