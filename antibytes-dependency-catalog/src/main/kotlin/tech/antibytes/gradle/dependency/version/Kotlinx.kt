/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

internal object Kotlinx {
    /**
     * (AtomicFu)(https://github.com/Kotlin/kotlinx.atomicfu)
     */
    const val atomicFu = "0.18.3"

    /**
     * [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
     */
    val coroutines = Coroutines

    object Coroutines {
        private const val version = "1.6.4"

        const val core = version
        const val android = version
    }

    /**
     * [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
     */
    val serialization = Serialization

    object Serialization {
        private const val version = "1.4.0"

        const val core = version
        const val cbor = version
        const val json = version
        const val jsonOkio = version
        const val properties = version
        const val protobuf = version
    }

    /**
     * [Kotlin DateTime](https://github.com/Kotlin/kotlinx-datetime)
     */
    const val dateTime = "0.4.0"
}
