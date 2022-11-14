/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module.android

import tech.antibytes.gradle.dependency.Version

object Hilt {
    const val gradle = "com.google.dagger:hilt-android-gradle-plugin:${Version.google.hilt}"
    const val core = "com.google.dagger:hilt-android:${Version.google.hilt}"
    const val compiler = "com.google.dagger:hilt-compiler:${Version.google.hilt}"
    const val composeNavigation = "androidx.hilt:hilt-navigation-compose:${Version.google.hiltCompose}"
    const val test = "com.google.dagger:hilt-android-testing:${Version.google.hilt}"
}
