/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.packages.android

import tech.antibytes.gradle.dependency.MavenArtifact
import tech.antibytes.gradle.dependency.version.Android

internal object Google {
    val hilt = MavenArtifact(
        group = "com.google.dagger",
        id = "hilt-android-gradle-plugin",
        version = Android.hilt.core,
    )
}
