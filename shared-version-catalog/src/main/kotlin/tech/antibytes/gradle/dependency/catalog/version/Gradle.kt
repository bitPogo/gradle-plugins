/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.GradleBundleVersion
import tech.antibytes.gradle.dependency.config.GradleVersions

internal object Gradle {
    /**
     * [Gradle Versions](https://github.com/ben-manes/gradle-versions-plugin)
     */
    val dependencyUpdate = GradleBundleVersion(
        GradleVersions.dependencyUpdate,
    )

    /**
     * [OWASP](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)
     */
    val owasp = GradleBundleVersion(
        GradleVersions.owasp,
    )

    /**
     * [Jacoco](https://github.com/jacoco/jacoco/releases)
     */
    val jacoco = GradleBundleVersion(
        GradleVersions.jacoco,
    )

    /**
     * [JGit Core](https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit)
     */
    val publishing = GradleVersions.publishing

    /**
     * [gradle-git-version](https://github.com/palantir/gradle-git-version/releases)
     */
    val versioning = GradleBundleVersion(
        GradleVersions.versioning,
    ) // see: https://github.com/palantir/gradle-git-version/issues/353

    /**
     * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
     */
    val spotless = GradleBundleVersion(
        GradleVersions.spotless,
    )

    /**
     * [KTLint](https://github.com/pinterest/ktlint)
     */
    const val ktlint = GradleVersions.ktlint

    /**
     * [KSP](https://github.com/google/ksp)
     */
    /**
     * [KSP DevTools on MavenCentral](https://mvnrepository.com/artifact/com.google.devtools.ksp/com.google.devtools.ksp.gradle.plugin)
     */
    // GradleVersions.dependencyUpdate
    val ksp = GradleBundleVersion(
        GradleVersions.ksp,
    )
}
