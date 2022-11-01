/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages

import tech.antibytes.gradle.dependency.Version
import tech.antibytes.gradle.dependency.packages.android.AppCompact
import tech.antibytes.gradle.dependency.packages.android.Coil
import tech.antibytes.gradle.dependency.packages.android.Compose
import tech.antibytes.gradle.dependency.packages.android.Hilt
import tech.antibytes.gradle.dependency.packages.android.Ktx
import tech.antibytes.gradle.dependency.packages.android.Test

object Android {
    // AGP
    // const val androidGradlePlugin = "com.android.tools.build:gradle:${Version.android.androidGradlePlugin}"

    // Android
    const val desugar = "com.android.tools:desugar_jdk_libs:${Version.android.desugar}"
    
    // AndroidX
    val ktx = Ktx

    val appCompact = AppCompact

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Version.android.constraintLayout}"

    // Material
    const val material = "com.google.android.material:material:${Version.android.material}"

    val compose = Compose

    val test = Test

    val coil = Coil

    val hilt = Hilt
}
