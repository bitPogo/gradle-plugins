/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.config.GradleVersions

internal object Square {
    /**
     * [Okio](https://square.github.io/okio/)
     */
    val okio = Okio

    internal object Okio {
        private const val version = GradleVersions.squareOkio
        const val core = version
        const val fakefilesystem = version
        const val nodefilesystem = version
        const val bom = version
    }

    /**
     * [SQLDelight](https://github.com/cashapp/sqldelight/)
     */
    val sqldelight = SqlDelight

    internal object SqlDelight {
        private const val version = GradleVersions.squareSqldelight
        val driver = Driver

        internal object Driver {
            const val android = version
            const val jdbc = version
            const val jvm = version
            const val js = version
            const val native = version
        }

        const val primitiveAdapters = version
        const val coroutines = version
        const val gradle = version
        const val plugin = version
    }

    /**
     * [OkHttp](https://square.github.io/okhttp/)
     */
    val okhttp = OkHttp

    internal object OkHttp {
        private const val version = GradleVersions.squareOkhttp
        const val core = version

        const val coroutines = version
        const val bom = version
        const val brotli = version
        const val dnsOverHttps = version
        const val logger = version
        const val tls = version
        const val urlConnection = version
        const val curl = version
        const val sse = version

        val mockserver = Mockserver

        internal object Mockserver {
            const val core = version
            const val junit4 = version
            const val junit5 = version
        }
    }

    /**
     * [KotlinPoet](https://github.com/square/kotlinpoet)
     */
    val kotlinPoet = KotlinPoet

    internal object KotlinPoet {
        private const val version = GradleVersions.kotlinPoet

        const val core = version
        const val ksp = version
    }
}
