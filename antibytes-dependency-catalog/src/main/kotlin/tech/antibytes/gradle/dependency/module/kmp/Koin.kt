/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.kmp

import tech.antibytes.gradle.dependency.Version

object Koin {
    const val core = "io.insert-koin:koin-core:${Version.kotlin.koin}"
    const val android = "io.insert-koin:koin-android:${Version.kotlin.koin}"
    const val js = "io.insert-koin:koin-core-js:${Version.kotlin.koin}"
    const val jvm = "io.insert-koin:koin-core-jvm:${Version.kotlin.koin}"
}
