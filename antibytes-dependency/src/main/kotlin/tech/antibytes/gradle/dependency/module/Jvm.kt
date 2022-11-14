/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.Version

object Jvm {
    val slf4j = Slf4j

    object Slf4j {
        const val api = "org.slf4j:slf4j-api:${Version.jvm.slf4j}"
        const val noop = "org.slf4j:slf4j-nop:${Version.jvm.slf4j}"
    }

    val test = Test

    object Test {
        const val kotlin = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin.stdlib}"
        const val junit = "org.junit:junit-bom:${Version.jvm.test.junit}"
        const val jupiter = "org.junit.jupiter:junit-jupiter"

        val mockk = Mockk
        const val koin = "io.insert-koin:koin-test:${Version.kotlin.koin}"

        object Mockk {
            const val unit = "io.mockk:mockk:${Version.kotlin.test.mockk}"
            const val instrumented = "io.mockk:mockk-jvm:${Version.kotlin.test.mockk}"
        }
    }
}
