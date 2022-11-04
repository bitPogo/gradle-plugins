/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.Version

object Hilt {
    const val gradle = "com.google.dagger:hilt-android-gradle-plugin:${Version.android.hilt.core}"
    const val core = "com.google.dagger:hilt-android:${Version.android.hilt.core}"
    const val compiler = "com.google.dagger:hilt-compiler:${Version.android.hilt.core}"
    const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Version.android.hilt.compose}"
    const val test = "com.google.dagger:hilt-android-testing:${Version.android.hilt.core}"
}
