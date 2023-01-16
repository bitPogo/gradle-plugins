/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.catalog.module.gradle

import tech.antibytes.gradle.dependency.catalog.GradleArtifact
import tech.antibytes.gradle.dependency.catalog.GradleBundle
import tech.antibytes.gradle.dependency.catalog.GradleTestArtifact

internal object Antibytes {
    private const val group = "tech.antibytes.gradle"

    val dependencyHelper = GradleBundle(
        group = "$group.dependency.helper",
        id = "antibytes-dependency-helper",
        plugin = "tech.antibytes.gradle.dependency.helper",
    )
    val nodeHelper = GradleArtifact(
        group = "$group.dependency.node",
        id = "antibytes-dependency-node",
    )
    val publishing = GradleBundle(
        group = "$group.publishing",
        id = "antibytes-publishing",
        plugin = "tech.antibytes.gradle.publishing",
    )
    val versioning = GradleBundle(
        group = "$group.versioning",
        id = "antibytes-versioning",
        plugin = "tech.antibytes.gradle.versioning",
    )
    val coverage = GradleBundle(
        group = "$group.coverage",
        id = "antibytes-coverage",
        plugin = "tech.antibytes.gradle.coverage",
    )

    val androidLibraryConfiguration = GradleBundle(
        group = "$group.configuration.android.library",
        id = "antibytes-android-library-configuration",
        plugin = "tech.antibytes.gradle.configuration.android.library",
    )
    val androidApplicationConfiguration = GradleBundle(
        group = "$group.configuration.android.application",
        id = "antibytes-android-application-configuration",
        plugin = "tech.antibytes.gradle.configuration.android.application",
    )
    val publishingConfiguration = GradleArtifact(
        group = "$group.configuration.publishing",
        id = "antibytes-publishing-configuration",
    )
    val dokkaConfiguration = GradleBundle(
        group = "$group.configuration.dokka",
        id = "antibytes-dokka-configuration",
        plugin = "tech.antibytes.gradle.configuration.dokka",
    )
    val kmpConfiguration = GradleBundle(
        group = "$group.configuration.kmp",
        id = "antibytes-kmp-configuration",
        plugin = "tech.antibytes.gradle.configuration.kmp",
    )

    val quality = GradleBundle(
        group = "$group.quality",
        id = "antibytes-quality",
        plugin = "tech.antibytes.gradle.quality",
    )
    val runtimeConfig = GradleArtifact(
        group = group,
        id = "antibytes-runtime-configuration",
    )
    val grammarTools = GradleBundle(
        group = "$group.grammar",
        id = "antibytes-grammar-tools",
        plugin = "tech.antibytes.gradle.grammar",
    )
    val utils = GradleArtifact(
        group = group,
        id = "antibytes-gradle-utils",
    )
    val testUtils = GradleTestArtifact(
        group = group,
        id = "antibytes-gradle-test-utils",
    )
    val customComponent = GradleBundle(
        group = "$group.custom-component",
        id = "antibytes-custom-component",
        plugin = "tech.antibytes.gradle.component",
    )
    val detektConfiguration = GradleArtifact(
        group = group,
        id = "antibytes-detekt-configuration",
    )
    val mkDocs = GradleBundle(
        group = "$group.mkdocs",
        id = "antibytes-mkdocs",
        plugin = "tech.antibytes.gradle.mkdocs",
    )
}
