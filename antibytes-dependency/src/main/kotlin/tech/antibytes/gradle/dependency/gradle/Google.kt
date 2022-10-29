/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.gradle

import tech.antibytes.gradle.dependency.MavenArtifact

internal object Google {
    val hilt = MavenArtifact(
        "com.google.dagger",
        "hilt-android-gradle-plugin",
    )
}
