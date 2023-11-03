/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module

import tech.antibytes.gradle.dependency.catalog.GradleArtifact
import tech.antibytes.gradle.dependency.catalog.GradleBundle
import tech.antibytes.gradle.dependency.catalog.module.gradle.Antibytes
import tech.antibytes.gradle.dependency.catalog.module.gradle.KSP

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
    val toolchainResolver = GradleBundle(
        group = "org.gradle.toolchains",
        id = "foojay-resolver",
        plugin = "org.gradle.toolchains.foojay-resolver-convention",
    )
    val ksp = KSP
    val antibytes = Antibytes
}
