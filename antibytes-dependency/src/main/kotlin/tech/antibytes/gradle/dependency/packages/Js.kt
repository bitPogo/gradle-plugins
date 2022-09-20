/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages

import tech.antibytes.gradle.dependency.Version

object Js {
    const val nodejs = "org.jetbrains.kotlinx:kotlinx-nodejs:${Version.JS.nodeJs}"

    val test = Test

    object Test {
        const val js = "org.jetbrains.kotlin:kotlin-test-js:${Version.kotlin.stdlib}"
    }
}
