/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

object Kotlin {
    /**
     * [Kotlin](https://github.com/JetBrains/kotlin)
     */
    const val language = "1.7.10"

    const val stdlib = language

    /**
     * [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
     */
    const val coroutines = "1.6.4"

    /**
     * [Koin](https://github.com/InsertKoinIO/koin)
     */
    const val koin = "3.2.0"

    /**
     * [Ktor](https://github.com/ktorio/ktor)
     */
    const val ktor = "2.1.1"

    /**
     * [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
     */
    const val serialization = "1.4.0"

    /**
     * [Kotlin DateTime](https://github.com/Kotlin/kotlinx-datetime)
     */
    const val dateTime = "0.4.0"

    /**
     * [UUID](https://github.com/benasher44/uuid)
     */
    const val uuid = "0.5.0"

    /**
     * [Stately](https://github.com/touchlab/Stately)
     */
    const val stately = "1.2.3"

    /**
     * (AtomicFu)(https://github.com/Kotlin/kotlinx.atomicfu)
     */
    const val atomicFu = "0.18.3"

    val test = Test

    object Test {
        /**
         * [mockk](http://mockk.io)
         */
        const val mockk = "1.12.8"
    }
}
