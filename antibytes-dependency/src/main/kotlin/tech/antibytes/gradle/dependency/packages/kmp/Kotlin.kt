/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages.kmp

import tech.antibytes.gradle.dependency.Version

object Kotlin {
    const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Version.kotlin.stdlib}"
    const val jdk = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
    const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Version.kotlin.stdlib}"
    const val js = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
    const val native = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
    const val android = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin.stdlib}"
}
