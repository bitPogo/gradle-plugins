/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.version

import tech.antibytes.gradle.dependency.GradleBundleVersion

internal object Gradle {
    /**
     * [Gradle Versions](https://github.com/ben-manes/gradle-versions-plugin)
     */
    val dependencyUpdate = GradleBundleVersion("0.42.0")

    /**
     * [OWASP](https://plugins.gradle.org/plugin/org.owasp.dependencycheck)
     */
    val owasp = GradleBundleVersion("7.2.0")

    /**
     * [Jacoco](https://github.com/jacoco/jacoco/releases)
     */
    val jacoco = GradleBundleVersion("0.8.8")

    /**
     * [JGit Core](https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit)
     */
    val publishing = "6.3.0.202209071007-r"

    /**
     * [gradle-git-version](https://github.com/palantir/gradle-git-version/releases)
     */
    val versioning = GradleBundleVersion("0.15.0") // see: https://github.com/palantir/gradle-git-version/issues/353

    /**
     * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
     */
    val spotless = GradleBundleVersion("6.11.0")

    /**
     * [KTLint](https://github.com/pinterest/ktlint)
     */
    const val ktlint = "0.47.1"
}
