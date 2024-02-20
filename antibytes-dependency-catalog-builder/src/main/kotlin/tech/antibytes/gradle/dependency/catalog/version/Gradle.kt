/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.catalog.version

import tech.antibytes.gradle.dependency.catalog.GradleBundleVersion
import tech.antibytes.gradle.dependency.config.GradleVersions
import tech.antibytes.gradle.dependency.config.MainConfig

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

    const val gradle = GradleVersions.gradle

    /**
     * [ToolChain Resolver](https://plugins.gradle.org/plugin/org.gradle.toolchains.foojay-resolver-convention
     */
    val toolchainResolver = GradleBundleVersion(GradleVersions.toolchainResolver)

    /**
     * [KSP](https://github.com/google/ksp)
     */
    /**
     * [KSP DevTools on MavenCentral](https://mvnrepository.com/artifact/com.google.devtools.ksp/com.google.devtools.ksp.gradle.plugin)
     */
    val ksp = KSP
    internal object KSP {
        private const val version = GradleVersions.ksp
        val plugin = GradleBundleVersion(version)
        const val runtime = version
    }

    val antibytes = Antibytes

    internal object Antibytes {
        private const val version = MainConfig.antibytes

        val dependencyHelper = GradleBundleVersion(version)
        const val nodeHelper = version
        val publishing = GradleBundleVersion(version)
        val versioning = GradleBundleVersion(version)
        val coverage = GradleBundleVersion(version)
        val androidLibraryConfiguration = GradleBundleVersion(version)
        val androidApplicationConfiguration = GradleBundleVersion(version)
        const val publishingConfiguration = version
        val dokkaConfiguration = GradleBundleVersion(version)
        val kmpConfiguration = GradleBundleVersion(version)
        val quality = GradleBundleVersion(version)
        const val runtimeConfig = version
        val grammarTools = GradleBundleVersion(version)
        const val utils = version
        const val testUtils = version
        val customComponent = GradleBundleVersion(version)
        const val detektConfiguration = version
        val mkDocs = GradleBundleVersion(version)
    }
}
