/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

internal object Gradle {
    val agp = MavenArtifact(
        group = "com.android.tools.build",
        id = "gradle",
        version = Version.android.androidGradlePlugin
    )
    val dependencyUpdate = MavenArtifact(
        group = "com.github.ben-manes",
        id = "gradle-versions-plugin",
        version = "Version.gradle"
    )
}
