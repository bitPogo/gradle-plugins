/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

import tech.antibytes.gradle.dependency.version.ktor.Client
import tech.antibytes.gradle.dependency.version.ktor.Serialization
import tech.antibytes.gradle.dependency.version.ktor.Server

internal object Ktor {
    /**
     * [Ktor](https://github.com/ktorio/ktor)
     */
    internal const val version = "2.1.1"

    val client = Client

    const val events = version
    const val http = version
    const val httpCio = version
    const val io = version
    const val network = version
    const val networkTls = version
    const val networkTlsCertificates = version

    val serialization = Serialization

    val server = Server

    const val utils = version
    const val websocketSerialization = version
    const val websockets = version
}
