/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import tech.antibytes.gradle.dependency.module.Android
import tech.antibytes.gradle.dependency.module.Js
import tech.antibytes.gradle.dependency.module.Jvm
import tech.antibytes.gradle.dependency.module.Multiplatform
import tech.antibytes.gradle.dependency.module.Npm

object Dependency {

    val multiplatform = Multiplatform

    val jvm = Jvm

    val android = Android

    val js = Js

    val npm = Npm
}
