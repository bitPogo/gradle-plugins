/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.config.GradleVersions

internal object Kotlinx {
    /**
     * (AtomicFu)(https://github.com/Kotlin/kotlinx.atomicfu)
     */
    val atomicfu = AtomicFu

    internal object AtomicFu {
        private const val version = GradleVersions.kotlinxAtomicfu

        const val core = version
        const val gradle = version
        const val plugin = version
    }

    /**
     * [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
     */
    val coroutines = Coroutines

    object Coroutines {
        private const val version = GradleVersions.kotlinxCoroutines

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
        private const val version = GradleVersions.kotlinxSerialization

        const val bom = version
        const val core = version
        const val cbor = version
        const val json = version
        const val jsonOkio = version
        const val hocon = version
        const val properties = version
        const val protobuf = version
        const val gradle = Kotlin.language
        const val plugin = Kotlin.language
    }

    /**
     * [Kotlin DateTime](https://github.com/Kotlin/kotlinx-datetime)
     */
    const val dateTime = GradleVersions.kotlinxDateTime

    /**
     * [KotlinNodeJS](https://github.com/Kotlin/kotlinx-nodejs)
     */
    const val nodeJs = GradleVersions.kotlinxNodeJs
}
