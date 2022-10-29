/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages

import tech.antibytes.gradle.dependency.Version
import tech.antibytes.gradle.dependency.packages.kmp.AtomicFu
import tech.antibytes.gradle.dependency.packages.kmp.Coroutines
import tech.antibytes.gradle.dependency.packages.kmp.Koin
import tech.antibytes.gradle.dependency.packages.kmp.Kotlin
import tech.antibytes.gradle.dependency.packages.kmp.Ktor
import tech.antibytes.gradle.dependency.packages.kmp.Serialization
import tech.antibytes.gradle.dependency.packages.kmp.Stately
import tech.antibytes.gradle.dependency.packages.kmp.Test

object Multiplatform {
    val kotlin = Kotlin

    val coroutines = Coroutines

    val atomicFu = AtomicFu

    val koin = Koin

    val ktor = Ktor

    val serialization = Serialization

    const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Version.kotlin.dateTime}"

    const val uuid = "com.benasher44:uuid:${Version.kotlin.uuid}"

    val stately = Stately

    val square = Square

    val test = Test
}
