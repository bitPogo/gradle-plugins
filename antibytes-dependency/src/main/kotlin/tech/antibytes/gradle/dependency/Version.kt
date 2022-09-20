/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import tech.antibytes.gradle.dependency.version.Android
import tech.antibytes.gradle.dependency.version.Google
import tech.antibytes.gradle.dependency.version.Gradle
import tech.antibytes.gradle.dependency.version.Jvm
import tech.antibytes.gradle.dependency.version.Kotlin
import tech.antibytes.gradle.dependency.version.Npm
import tech.antibytes.gradle.dependency.version.Square

object Version {

    val gradle = Gradle

    // Kotlin
    val kotlin = Kotlin

    val square = Square

    val android = Android

    val jvm = Jvm

    val google = Google

    val js = JS

    val npm = Npm

    object JS {
        /**
         * [KotlinNodeJS](https://github.com/Kotlin/kotlinx-nodejs)
         */
        const val nodeJs = "0.0.7"
        const val node = "18.9.0"
    }
}
