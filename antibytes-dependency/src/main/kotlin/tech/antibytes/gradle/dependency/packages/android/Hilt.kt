/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages.android

import tech.antibytes.gradle.dependency.Version

object Hilt {
    const val gradle = "com.google.dagger:hilt-android-gradle-plugin:${Version.google.hilt.core}"
    const val core = "com.google.dagger:hilt-android:${Version.google.hilt.core}"
    const val compiler = "com.google.dagger:hilt-compiler:${Version.google.hilt.core}"
    const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Version.google.hilt.compose}"
    const val test = "com.google.dagger:hilt-android-testing:${Version.google.hilt.core}"
}
