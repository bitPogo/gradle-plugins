/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.api

import java.io.File
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageApiContract.AndroidJacocoCoverageConfigurationProvider
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract
import tech.antibytes.gradle.coverage.configuration.value.AndroidConfigurationProvider
import tech.antibytes.gradle.util.GradleUtilApiContract

data class AndroidJacocoConfiguration(
    override val reportSettings: CoverageApiContract.JacocoReporterSettings,
    override var test: Set<String>,
    override var classPattern: Set<String>,
    override var classFilter: Set<String>,
    override var sources: Set<File>,
    override var additionalSources: Set<File>,
    override var additionalClasses: ConfigurableFileTree? = null,
    override var verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
    override var instrumentedTest: Set<String>,
    override var variant: String,
    override var flavour: String,
) : CoverageApiContract.AndroidJacocoCoverageConfiguration {
    internal object Provider : ConfigurationContract.DefaultAndroidConfigurationProvider {
        private val provider = AndroidConfigurationProvider()

        override fun createDefaultCoverageConfiguration(
            project: Project,
            context: GradleUtilApiContract.PlatformContext,
            variant: String,
            flavour: String,
        ): CoverageApiContract.CoverageConfiguration = provider.createDefaultCoverageConfiguration(
            project = project,
            context = context,
            variant = variant,
            flavour = flavour,
        )
    }

    companion object : AndroidJacocoCoverageConfigurationProvider {
        private fun overrideDefaults(
            configuration: AndroidJacocoConfiguration,
            variant: String,
            flavour: String,
            reportSettings: CoverageApiContract.JacocoReporterSettings?,
            testDependencies: Set<String>,
            instrumentedTestDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): AndroidJacocoConfiguration {
            if (variant.isNotEmpty()) {
                configuration.variant = variant
            }

            if (flavour.isNotEmpty()) {
                configuration.flavour = flavour
            }

            if (testDependencies.isNotEmpty()) {
                configuration.test = testDependencies
            }

            if (instrumentedTestDependencies.isNotEmpty()) {
                configuration.instrumentedTest = instrumentedTestDependencies
            }

            if (classPattern.isNotEmpty()) {
                configuration.classPattern = classPattern
            }

            if (classFilter.isNotEmpty()) {
                configuration.classFilter = classFilter
            }

            if (sources.isNotEmpty()) {
                configuration.sources = sources
            }

            if (additionalSources.isNotEmpty()) {
                configuration.additionalSources = additionalSources
            }

            if (additionalClasses is ConfigurableFileTree) {
                configuration.additionalClasses = additionalClasses
            }

            if (verificationRules.isNotEmpty()) {
                configuration.verificationRules = verificationRules
            }

            return if (reportSettings is CoverageApiContract.JacocoReporterSettings) {
                configuration.copy(reportSettings = reportSettings)
            } else {
                configuration
            }
        }

        private fun createConfiguration(
            project: Project,
            context: GradleUtilApiContract.PlatformContext,
            variant: String,
            flavour: String,
            reportSettings: CoverageApiContract.JacocoReporterSettings?,
            testDependencies: Set<String>,
            instrumentedTestDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): CoverageApiContract.AndroidJacocoCoverageConfiguration {
            val config = Provider.createDefaultCoverageConfiguration(
                project,
                context,
            )

            return overrideDefaults(
                config as AndroidJacocoConfiguration,
                variant,
                flavour,
                reportSettings,
                testDependencies,
                instrumentedTestDependencies,
                classPattern,
                classFilter,
                sources,
                additionalSources,
                additionalClasses,
                verificationRules,
            )
        }

        override fun createAndroidAppOnlyConfiguration(
            project: Project,
            variant: String,
            flavour: String,
            reportSettings: CoverageApiContract.JacocoReporterSettings?,
            testDependencies: Set<String>,
            instrumentedTestDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): CoverageApiContract.AndroidJacocoCoverageConfiguration {
            return createConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
                variant,
                flavour,
                reportSettings,
                testDependencies,
                instrumentedTestDependencies,
                classPattern,
                classFilter,
                sources,
                additionalSources,
                additionalClasses,
                verificationRules,
            )
        }

        override fun createAndroidAppKmpConfiguration(
            project: Project,
            variant: String,
            flavour: String,
            reportSettings: CoverageApiContract.JacocoReporterSettings?,
            testDependencies: Set<String>,
            instrumentedTestDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): CoverageApiContract.AndroidJacocoCoverageConfiguration {
            return createConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION_KMP,
                variant,
                flavour,
                reportSettings,
                testDependencies,
                instrumentedTestDependencies,
                classPattern,
                classFilter,
                sources,
                additionalSources,
                additionalClasses,
                verificationRules,
            )
        }

        override fun createAndroidLibraryOnlyConfiguration(
            project: Project,
            variant: String,
            flavour: String,
            reportSettings: CoverageApiContract.JacocoReporterSettings?,
            testDependencies: Set<String>,
            instrumentedTestDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): CoverageApiContract.AndroidJacocoCoverageConfiguration {
            return createConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY,
                variant,
                flavour,
                reportSettings,
                testDependencies,
                instrumentedTestDependencies,
                classPattern,
                classFilter,
                sources,
                additionalSources,
                additionalClasses,
                verificationRules,
            )
        }

        override fun createAndroidLibraryKmpConfiguration(
            project: Project,
            variant: String,
            flavour: String,
            reportSettings: CoverageApiContract.JacocoReporterSettings?,
            testDependencies: Set<String>,
            instrumentedTestDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): CoverageApiContract.AndroidJacocoCoverageConfiguration {
            return createConfiguration(
                project,
                GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP,
                variant,
                flavour,
                reportSettings,
                testDependencies,
                instrumentedTestDependencies,
                classPattern,
                classFilter,
                sources,
                additionalSources,
                additionalClasses,
                verificationRules,
            )
        }
    }
}
