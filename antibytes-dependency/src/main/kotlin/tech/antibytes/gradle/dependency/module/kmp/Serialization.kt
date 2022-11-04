/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.kmp

import tech.antibytes.gradle.dependency.Version

object Serialization {
    const val common = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Version.kotlin.serialization}"
    const val android = "org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:${Version.kotlin.serialization}"
    const val protobuf = "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:${Version.kotlin.serialization}"
    const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlin.serialization}"
}
