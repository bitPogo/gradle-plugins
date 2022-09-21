/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages.android

import tech.antibytes.gradle.dependency.Version

object AppCompact {
    const val core = "androidx.appcompat:appcompat:${Version.android.appCompat}"
    const val resources = "androidx.appcompat:appcompat-resources:${Version.android.appCompat}"
}
