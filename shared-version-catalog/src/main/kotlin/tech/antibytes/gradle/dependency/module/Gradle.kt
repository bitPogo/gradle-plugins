/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.module

import tech.antibytes.gradle.dependency.GradleArtifact
import tech.antibytes.gradle.dependency.GradleBundle

internal object Gradle {
    val dependencyUpdate = GradleBundle(
        group = "com.github.ben-manes",
        id = "gradle-versions-plugin",
        plugin = "com.github.ben-manes.versions",
    )

    val owasp = GradleBundle(
        group = "org.owasp",
        id = "dependency-check-gradle",
        plugin = "org.owasp.dependencycheck",
    )
    val jacoco = GradleBundle(
        group = "org.jacoco",
        id = "org.jacoco.core",
        plugin = "jacoco",
    )
    val publishing = GradleArtifact(
        group = "org.eclipse.jgit",
        id = "org.eclipse.jgit",
    )
    val versioning = GradleBundle(
        group = "com.palantir.gradle.gitversion",
        id = "gradle-git-version",
        plugin = "com.palantir.git-version",
    )
    val spotless = GradleBundle(
        group = "com.diffplug.spotless",
        id = "spotless-plugin-gradle",
        plugin = "com.diffplug.spotless",
    )
    val ktlint = GradleArtifact(
        group = "com.pinterest",
        id = "ktlint",
    )
}
