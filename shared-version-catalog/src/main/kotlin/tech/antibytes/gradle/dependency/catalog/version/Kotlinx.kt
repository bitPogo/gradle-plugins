/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.version.Kotlin.language

internal object Kotlinx {
    /**
     * (AtomicFu)(https://github.com/Kotlin/kotlinx.atomicfu)
     */
    val atomicfu = AtomicFu

    internal object AtomicFu {
        private const val version = "0.18.3"

        const val core = version
        const val gradle = version
        const val plugin = version
    }

    /**
     * [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
     */
    val coroutines = Coroutines

    object Coroutines {
        private const val version = "1.6.4"

        const val bom = version
        const val core = version
        const val android = version
        const val javafx = version
        const val jdk8 = version
        const val jdk9 = version
        const val test = version
    }

    /**
     * [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
     */
    val serialization = Serialization

    object Serialization {
        private const val version = "1.4.0"

        const val bom = version
        const val core = version
        const val cbor = version
        const val json = version
        const val jsonOkio = version
        const val hocon = version
        const val properties = version
        const val protobuf = version
        const val gradle = language
        const val plugin = language
    }

    /**
     * [Kotlin DateTime](https://github.com/Kotlin/kotlinx-datetime)
     */
    const val dateTime = "0.4.0"

    /**
     * [KotlinNodeJS](https://github.com/Kotlin/kotlinx-nodejs)
     */
    const val nodeJs = "0.0.7"
}