/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration.value

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEFAULT_ANDROID_FLAVOUR
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEFAULT_ANDROID_MARKER
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEFAULT_ANDROID_VARIANT
import tech.antibytes.gradle.coverage.api.AndroidJacocoConfiguration
import tech.antibytes.gradle.coverage.api.JacocoReporterSettings
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract.PlatformContext
import tech.antibytes.gradle.coverage.configuration.makePath
import tech.antibytes.gradle.coverage.source.SourceHelper

internal object AndroidConfigurationProvider : ConfigurationContract.DefaultPlatformConfigurationProvider {
    private fun resolveTestDependency(): Set<String> = setOf("test${DEFAULT_ANDROID_MARKER}UnitTest")

    private fun resolveInstrumentedTestTasks(context: PlatformContext): Set<String> {
        return if (context == PlatformContext.ANDROID_LIBRARY || context == PlatformContext.ANDROID_LIBRARY_KMP) {
            emptySet()
        } else {
            setOf("connected${DEFAULT_ANDROID_MARKER}AndroidTest")
        }
    }

    private fun resolveClassPattern(): Set<String> {
        return setOf(
            makePath("build", "intermediates", "javac", DEFAULT_ANDROID_VARIANT, "**", "*.class"),
            makePath("build", "tmp", "kotlin-classes", DEFAULT_ANDROID_VARIANT, "**", "*.class")
        )
    }

    private fun resolveClassFilter(): Set<String> {
        return setOf(
            makePath("**", "R.class"),
            makePath("**", "R\$*.class"),
            makePath("**/BuildConfig.*"),
            makePath("**", "Manifest*.*"),
            makePath("**", "*Test*.*"),
            makePath("android", "**", "*.*"),
            makePath("**", "*\$Lambda\$*.*"),
            makePath("**", "*\$inlined\$*.*")
        )
    }

    override fun createDefaultCoverageConfiguration(
        project: Project,
        context: PlatformContext
    ): CoverageApiContract.AndroidJacocoCoverageConfiguration {
        return AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(),
            testDependencies = resolveTestDependency(),
            classPattern = resolveClassPattern(),
            classFilter = resolveClassFilter(),
            sources = SourceHelper.resolveSources(project, context),
            additionalClasses = emptySet(),
            additionalSources = emptySet(),
            verificationRules = emptySet(),
            instrumentedTestDependencies = resolveInstrumentedTestTasks(context),
            variant = DEFAULT_ANDROID_VARIANT,
            flavour = DEFAULT_ANDROID_FLAVOUR
        )
    }
}
