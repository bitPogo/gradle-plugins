/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import tech.antibytes.gradle.dependency.packages.Android
import tech.antibytes.gradle.dependency.packages.Js
import tech.antibytes.gradle.dependency.packages.Jvm
import tech.antibytes.gradle.dependency.packages.Multiplatform

object Dependency {

    val multiplatform = Multiplatform

    val jvm = Jvm

    val android = Android

    val js = Js
}
