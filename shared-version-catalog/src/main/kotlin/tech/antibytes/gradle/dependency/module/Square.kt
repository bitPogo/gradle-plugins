/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.module.square.OkHttp
import tech.antibytes.gradle.dependency.module.square.Okio
import tech.antibytes.gradle.dependency.module.square.SqlDelight

internal object Square {
    val okio = Okio
    val sqldelight = SqlDelight
    val okhttp = OkHttp
}
