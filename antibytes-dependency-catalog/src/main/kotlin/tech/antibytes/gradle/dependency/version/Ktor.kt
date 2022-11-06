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

    val serialisation = Serialization

    val server = Server

    val utils = version
}
