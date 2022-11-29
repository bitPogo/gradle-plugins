/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.api

import tech.antibytes.gradle.quality.QualityApiContract
import tech.antibytes.gradle.quality.QualityApiContract.LinterConfiguration.PartialConfiguration

data class PartialLinterConfiguration(
    override val include: Set<String>,
    override val exclude: Set<String>,
    override val configuration: Map<String, String>
) : PartialConfiguration

data class LinterConfiguration(
    override val code: PartialConfiguration = PartialLinterConfiguration(
        include = setOf("**/*.kt"),
        exclude = setOf(
            "buildSrc/build/",
            "**/buildSrc/build/",
            "**/src/test/resources/**/*.kt",
            "**/build/**/*.kt"
        ),
        configuration = mapOf(
            "ktlint_disabled_rules" to "no-wildcard-imports",
            "ij_kotlin_imports_layout" to "*",
            "ij_kotlin_allow_trailing_comma" to "true",
            "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
        )
    ),
    override val scripts: PartialConfiguration = PartialLinterConfiguration(
        include = setOf("*.gradle.kts"),
        exclude = emptySet(),
        configuration = emptyMap()
    ),
    override val misc: PartialConfiguration = PartialLinterConfiguration(
        include = setOf("**/*.adoc", "**/*.md", "**/.gitignore", ".java-version"),
        exclude = emptySet(),
        configuration = emptyMap()
    ),
) : QualityApiContract.LinterConfiguration
