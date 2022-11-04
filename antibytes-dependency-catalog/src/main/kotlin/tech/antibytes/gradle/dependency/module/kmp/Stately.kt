/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.kmp

import tech.antibytes.gradle.dependency.Version

object Stately {
    const val isolate = "co.touchlab:stately-isolate:${Version.kotlin.stately}"
    const val freeze = "co.touchlab:stately-common:${Version.kotlin.stately}"
    const val concurrency = "co.touchlab:stately-concurrency:${Version.kotlin.stately}"
    const val collections = "co.touchlab:stately-iso-collections:${Version.kotlin.stately}"
}
