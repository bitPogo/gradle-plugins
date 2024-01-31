/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
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
import tech.antibytes.gradle.coverage.configuration.makePath
import tech.antibytes.gradle.coverage.source.SourceHelper
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.decapitalize
import tech.antibytes.gradle.util.isAndroidLibrary

internal class AndroidConfigurationProvider(
    private val sourceHelper: SourceHelper = SourceHelper(),
) : ConfigurationContract.DefaultAndroidConfigurationProvider {
    private fun resolveTestDependency(): Set<String> = setOf("test${DEFAULT_ANDROID_MARKER}UnitTest")

    private fun resolveInstrumentedTestTasks(context: PlatformContext): Set<String> {
        return if (context.isAndroidLibrary()) {
            emptySet()
        } else {
            setOf("connected${DEFAULT_ANDROID_MARKER}AndroidTest")
        }
    }

    private fun resolveClassPattern(): Set<String> {
        return setOf(
            makePath("build", "intermediates", "javac", DEFAULT_ANDROID_MARKER.decapitalize(), "**", "*.class"),
            makePath("build", "tmp", "kotlin-classes", DEFAULT_ANDROID_MARKER.decapitalize(), "**", "*.class"),
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
            makePath("**", "*\$inlined\$*.*"),
        )
    }

    override fun createDefaultCoverageConfiguration(
        project: Project,
        context: PlatformContext,
        variant: String,
        flavour: String,
    ): CoverageApiContract.AndroidJacocoCoverageConfiguration {
        return AndroidJacocoConfiguration(
            reportSettings = JacocoReporterSettings(),
            test = resolveTestDependency(),
            classPattern = resolveClassPattern(),
            classFilter = resolveClassFilter(),
            sources = sourceHelper.resolveSources(project, context),
            additionalClasses = null,
            additionalSources = emptySet(),
            verificationRules = emptySet(),
            instrumentedTest = resolveInstrumentedTestTasks(context),
            variant = DEFAULT_ANDROID_VARIANT,
            flavour = DEFAULT_ANDROID_FLAVOUR,
        )
    }
}
