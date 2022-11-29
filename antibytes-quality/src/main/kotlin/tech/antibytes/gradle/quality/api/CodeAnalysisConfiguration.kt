/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.api

import org.gradle.api.JavaVersion
import tech.antibytes.gradle.quality.QualityApiContract
import tech.antibytes.gradle.quality.QualityApiContract.CodeAnalysisConfiguration.Report

data class CodeAnalysisReport(
    override val html: Boolean = true,
    override val xml: Boolean = true,
    override val txt: Boolean = false,
    override val sarif: Boolean = false,
) : Report

data class CodeAnalysisConfiguration(
    override val jvmVersion: String = JavaVersion.VERSION_11.toString(),
    override val autoCorrection: Boolean = false,
    override val exclude: Set<String> = setOf(
        "**/.gradle/**",
        "**/.idea/**",
        "**/build/**",
        "**/buildSrc/**",
        ".github/**",
        "gradle/**",
        "**/example/**",
        "**/test/resources/**",
        "**/build.gradle.kts",
        "**/settings.gradle.kts",
        "**/Dangerfile.df.kts",
    ),
    override val excludeBaseline: Set<String> = setOf(
        "**/.gradle/**",
        "**/.idea/**",
        "**/build/**",
        "**/gradle/wrapper/**",
        ".github/**",
        "assets/**",
        "docs/**",
        "gradle/**",
        "**/example/**",
        "**/*.adoc",
        "**/*.md",
        "**/gradlew",
        "**/LICENSE",
        "**/.java-version",
        "**/gradlew.bat",
        "**/*.png",
        "**/*.properties",
        "**/*.pro",
        "**/*.sq",
        "**/*.xml",
        "**/*.yml",
    ),
    override val reports: Report = CodeAnalysisReport(),
) : QualityApiContract.CodeAnalysisConfiguration
