/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.module.square.KotlinPoet
import tech.antibytes.gradle.dependency.catalog.module.square.OkHttp
import tech.antibytes.gradle.dependency.catalog.module.square.Okio
import tech.antibytes.gradle.dependency.catalog.module.square.SqlDelight

internal object Square {
    internal const val domain = "com.squareup"

    val okio = Okio
    val sqldelight = SqlDelight
    val okhttp = OkHttp
    val kotlinPoet = KotlinPoet
}
