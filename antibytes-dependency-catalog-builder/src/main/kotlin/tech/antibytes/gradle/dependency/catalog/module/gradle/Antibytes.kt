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
        group = group,
        id = "antibytes-dependency-helper",
        plugin = "tech.antibytes.gradle.dependency.helper",
    )
    val publishing = GradleBundle(
        group = group,
        id = "antibytes-publishing",
        plugin = "tech.antibytes.gradle.publishing",
    )
    val versioning = GradleBundle(
        group = group,
        id = "antibytes-versioning",
        plugin = "tech.antibytes.gradle.versioning",
    )
    val coverage = GradleBundle(
        group = group,
        id = "antibytes-coverage",
        plugin = "tech.antibytes.gradle.coverage",
    )
    val projectConfig = GradleBundle(
        group = group,
        id = "antibytes-configuration",
        plugin = "tech.antibytes.gradle.configuration",
    )
    val runtimeConfig = GradleArtifact(
        group = group,
        id = "antibytes-runtime-configuration",
    )
    val grammarTools = GradleBundle(
        group = group,
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
        group = group,
        id = "antibytes-custom-component",
        plugin = "tech.antibytes.gradle.component",
    )
    val detektConfiguration = GradleArtifact(
        group = group,
        id = "antibytes-detekt-configuration",
    )
    val mkDocs = GradleBundle(
        group = group,
        id = "antibytes-mkdocs",
        plugin = "tech.antibytes.gradle.mkdocs",
    )
}
