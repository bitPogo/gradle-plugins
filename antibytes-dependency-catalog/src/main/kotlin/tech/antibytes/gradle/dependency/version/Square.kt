/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

internal object Square {
    /**
     * (Okio)(https://square.github.io/okio/)
     */
    val okio = Okio

    internal object Okio {
        private const val version = "3.2.0"
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
        private const val version = "1.5.3"
        val driver = Driver

        internal object Driver {
            const val android = version
            const val jdbc = version
            const val jvm = version
            const val js = version
            const val native = version
        }

        const val coroutines = version
        const val gradle = version
        const val plugin = version
    }

    /**
     * [OkHttp](https://square.github.io/okhttp/)
     */
    val okhttp = OkHttp

    internal object OkHttp {
        private const val version = "4.10.0"
        const val core = version

        // const val coroutines = version
        const val bom = version
        const val brotli = version
        const val dnsoverhttps = version
        const val logger = version
        const val tls = version
        const val urlConnection = version
        const val curl = version
        const val sse = version

        val mockserver = Mockserver

        internal object Mockserver {
            const val core = version
            // const val junit4 = version
            // const val junit5 = version
        }
    }
}
