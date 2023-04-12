/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.api

import java.io.File
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoReporterSettings
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract
import tech.antibytes.gradle.coverage.configuration.value.JvmConfigurationProvider
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext

data class JvmJacocoConfiguration(
    override val reportSettings: JacocoReporterSettings,
    override var test: Set<String>,
    override var classPattern: Set<String>,
    override var classFilter: Set<String>,
    override var sources: Set<File>,
    override var additionalSources: Set<File>,
    override var additionalClasses: ConfigurableFileTree?,
    override var verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
) : CoverageApiContract.JacocoCoverageConfiguration {
    internal object Provider : ConfigurationContract.DefaultJvmConfigurationProvider {
        private val provider = JvmConfigurationProvider()

        override fun createDefaultCoverageConfiguration(
            project: Project,
            context: PlatformContext,
        ): CoverageApiContract.CoverageConfiguration = provider.createDefaultCoverageConfiguration(
            project = project,
            context = context,
        )
    }

    companion object : CoverageApiContract.JacocoCoverageConfigurationProvider {
        private fun overrideDefaults(
            configuration: JvmJacocoConfiguration,
            reportSettings: JacocoReporterSettings?,
            testDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): JvmJacocoConfiguration {
            if (testDependencies.isNotEmpty()) {
                configuration.test = testDependencies
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

            return if (reportSettings is JacocoReporterSettings) {
                configuration.copy(reportSettings = reportSettings)
            } else {
                configuration
            }
        }

        private fun createConfiguration(
            project: Project,
            context: PlatformContext,
            reportSettings: JacocoReporterSettings?,
            testDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): JvmJacocoConfiguration {
            val config = Provider.createDefaultCoverageConfiguration(
                project,
                context,
            )

            return overrideDefaults(
                config as JvmJacocoConfiguration,
                reportSettings,
                testDependencies,
                classPattern,
                classFilter,
                sources,
                additionalSources,
                additionalClasses,
                verificationRules,
            )
        }

        override fun createJvmOnlyConfiguration(
            project: Project,
            reportSettings: JacocoReporterSettings?,
            testDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): JvmJacocoConfiguration {
            return createConfiguration(
                project,
                PlatformContext.JVM,
                reportSettings,
                testDependencies,
                classPattern,
                classFilter,
                sources,
                additionalSources,
                additionalClasses,
                verificationRules,
            )
        }

        override fun createJvmKmpConfiguration(
            project: Project,
            reportSettings: JacocoReporterSettings?,
            testDependencies: Set<String>,
            classPattern: Set<String>,
            classFilter: Set<String>,
            sources: Set<File>,
            additionalSources: Set<File>,
            additionalClasses: ConfigurableFileTree?,
            verificationRules: Set<CoverageApiContract.JacocoVerificationRule>,
        ): JvmJacocoConfiguration {
            return createConfiguration(
                project,
                PlatformContext.JVM_KMP,
                reportSettings,
                testDependencies,
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
