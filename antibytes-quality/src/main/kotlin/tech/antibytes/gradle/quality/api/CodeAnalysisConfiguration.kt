/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.api

import java.io.File
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import tech.antibytes.gradle.dependency.helper.customArtifact
import tech.antibytes.gradle.quality.QualityApiContract
import tech.antibytes.gradle.quality.QualityApiContract.CodeAnalysisConfiguration.Report
import tech.antibytes.gradle.quality.config.MainConfig

data class DetektReport(
    override val html: Boolean = true,
    override val xml: Boolean = true,
    override val txt: Boolean = false,
    override val sarif: Boolean = false,
) : Report

data class CodeAnalysisConfiguration(
    override val jvmVersion: String = JavaVersion.VERSION_17.toString(),
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
    override val reports: Report = DetektReport(),
    override val configurationFiles: ConfigurableFileCollection,
    override val baselineFile: File,
    override val sourceFiles: ConfigurableFileCollection,
) : QualityApiContract.CodeAnalysisConfiguration {
    constructor(
        project: Project,
        jvmVersion: String = JavaVersion.VERSION_17.toString(),
        autoCorrection: Boolean = false,
        exclude: Set<String> = setOf(
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
        excludeBaseline: Set<String> = setOf(
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
        reports: Report = DetektReport(),
    ) : this(
        jvmVersion = jvmVersion,
        autoCorrection = autoCorrection,
        exclude = exclude,
        excludeBaseline = excludeBaseline,
        reports = reports,
        configurationFiles = project.files(project.customArtifact(MainConfig.remoteDetektConfig)),
        baselineFile = project.file("${project.projectDir}/detekt/baseline.xml"),
        sourceFiles = project.files(project.projectDir),
    )
}
