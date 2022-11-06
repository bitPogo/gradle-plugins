/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

internal object Kotlin {
    /**
     * [Kotlin](https://github.com/JetBrains/kotlin)
     */
    const val language = "1.7.10"

    const val stdlib = language

    /**
     * [Koin](https://github.com/InsertKoinIO/koin)
     */
    const val koin = "3.2.1"

    /**
     * [Ktor](https://github.com/ktorio/ktor)
     */
    const val ktor = "2.1.1"

    /**
     * [Stately](https://github.com/touchlab/Stately)
     */
    const val stately = "1.2.3"

    val test = Test

    object Test {
        /**
         * [mockk](http://mockk.io)
         */
        const val mockk = "1.12.8"
    }
}
