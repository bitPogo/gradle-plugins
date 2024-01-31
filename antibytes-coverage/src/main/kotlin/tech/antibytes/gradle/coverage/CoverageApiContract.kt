/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage

import java.io.File
import java.math.BigDecimal
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEFAULT_ANDROID_FLAVOUR
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEFAULT_ANDROID_VARIANT

interface CoverageApiContract {
    interface Agent

    interface JacocoAgent : Agent

    interface ReporterSettings

    interface JacocoReporterSettings : ReporterSettings, JacocoAgent {
        var useHtml: Boolean
        var useCsv: Boolean
        var useXml: Boolean
    }

    interface VerificationRule

    enum class JacocoScope {
        CLASS,
        METHOD,
        BUNDLE,
    }

    enum class JacocoCounter {
        LINE,
        BRANCH,
        CLASS,
        INSTRUCTION,
        METHOD,
    }

    enum class JacocoMeasurement(val value: String) {
        COVERED_RATIO("COVEREDRATIO"),
        COVERED_COUNT("COVEREDCOUNT"),
        MISSED_RATIO("MISSEDRATIO"),
        MISSED_COUNT("MISSEDCOUNT"),
        TOTAL_COUNT("TOTALCOUNT"),
    }

    interface JacocoVerificationRule : VerificationRule, JacocoAgent {
        val scope: JacocoScope
        val enable: Boolean
        val counter: JacocoCounter
        val measurement: JacocoMeasurement
        val minimum: BigDecimal?
        val maximum: BigDecimal?
        val includes: Set<String>
        val excludes: Set<String>
    }

    interface CoverageConfiguration
    interface CoverageConfigurationProvider

    interface JacocoCoverageConfiguration : CoverageConfiguration, JacocoAgent {
        val reportSettings: JacocoReporterSettings
        var test: Set<String>
        var classPattern: Set<String>
        var classFilter: Set<String>
        var sources: Set<File>
        var additionalSources: Set<File>
        var additionalClasses: ConfigurableFileTree?
        var verificationRules: Set<JacocoVerificationRule>
    }

    interface JacocoCoverageConfigurationProvider : CoverageConfigurationProvider {
        fun createJvmOnlyConfiguration(
            project: Project,
            reportSettings: JacocoReporterSettings? = null,
            testDependencies: Set<String> = emptySet(),
            classPattern: Set<String> = emptySet(),
            classFilter: Set<String> = emptySet(),
            sources: Set<File> = emptySet(),
            additionalSources: Set<File> = emptySet(),
            additionalClasses: ConfigurableFileTree? = null,
            verificationRules: Set<JacocoVerificationRule> = emptySet(),
        ): JacocoCoverageConfiguration

        fun createJvmKmpConfiguration(
            project: Project,
            reportSettings: JacocoReporterSettings? = null,
            testDependencies: Set<String> = emptySet(),
            classPattern: Set<String> = emptySet(),
            classFilter: Set<String> = emptySet(),
            sources: Set<File> = emptySet(),
            additionalSources: Set<File> = emptySet(),
            additionalClasses: ConfigurableFileTree? = null,
            verificationRules: Set<JacocoVerificationRule> = emptySet(),
        ): JacocoCoverageConfiguration
    }

    interface AndroidJacocoCoverageConfiguration : JacocoCoverageConfiguration {
        var instrumentedTest: Set<String>
        var variant: String
        var flavour: String
    }

    interface JacocoAggregationConfiguration : CoverageConfiguration, JacocoAgent {
        val reportSettings: JacocoReporterSettings
        var exclude: Set<String>
        var verificationRules: Set<JacocoVerificationRule>
    }

    interface AndroidJacocoAggregationConfiguration : JacocoAggregationConfiguration {
        var variant: String
        var flavour: String
    }

    interface AndroidJacocoCoverageConfigurationProvider : CoverageConfigurationProvider {
        fun createAndroidAppOnlyConfiguration(
            project: Project,
            variant: String = DEFAULT_ANDROID_VARIANT,
            flavour: String = DEFAULT_ANDROID_FLAVOUR,
            reportSettings: JacocoReporterSettings? = null,
            testDependencies: Set<String> = emptySet(),
            instrumentedTestDependencies: Set<String> = emptySet(),
            classPattern: Set<String> = emptySet(),
            classFilter: Set<String> = emptySet(),
            sources: Set<File> = emptySet(),
            additionalSources: Set<File> = emptySet(),
            additionalClasses: ConfigurableFileTree? = null,
            verificationRules: Set<JacocoVerificationRule> = emptySet(),
        ): AndroidJacocoCoverageConfiguration

        fun createAndroidAppKmpConfiguration(
            project: Project,
            variant: String = DEFAULT_ANDROID_VARIANT,
            flavour: String = DEFAULT_ANDROID_FLAVOUR,
            reportSettings: JacocoReporterSettings? = null,
            testDependencies: Set<String> = emptySet(),
            instrumentedTestDependencies: Set<String> = emptySet(),
            classPattern: Set<String> = emptySet(),
            classFilter: Set<String> = emptySet(),
            sources: Set<File> = emptySet(),
            additionalSources: Set<File> = emptySet(),
            additionalClasses: ConfigurableFileTree? = null,
            verificationRules: Set<JacocoVerificationRule> = emptySet(),
        ): AndroidJacocoCoverageConfiguration

        fun createAndroidLibraryOnlyConfiguration(
            project: Project,
            variant: String = DEFAULT_ANDROID_VARIANT,
            flavour: String = DEFAULT_ANDROID_FLAVOUR,
            reportSettings: JacocoReporterSettings? = null,
            testDependencies: Set<String> = emptySet(),
            instrumentedTestDependencies: Set<String> = emptySet(),
            classPattern: Set<String> = emptySet(),
            classFilter: Set<String> = emptySet(),
            sources: Set<File> = emptySet(),
            additionalSources: Set<File> = emptySet(),
            additionalClasses: ConfigurableFileTree? = null,
            verificationRules: Set<JacocoVerificationRule> = emptySet(),
        ): AndroidJacocoCoverageConfiguration

        fun createAndroidLibraryKmpConfiguration(
            project: Project,
            variant: String = DEFAULT_ANDROID_VARIANT,
            flavour: String = DEFAULT_ANDROID_FLAVOUR,
            reportSettings: JacocoReporterSettings? = null,
            testDependencies: Set<String> = emptySet(),
            instrumentedTestDependencies: Set<String> = emptySet(),
            classPattern: Set<String> = emptySet(),
            classFilter: Set<String> = emptySet(),
            sources: Set<File> = emptySet(),
            additionalSources: Set<File> = emptySet(),
            additionalClasses: ConfigurableFileTree? = null,
            verificationRules: Set<JacocoVerificationRule> = emptySet(),
        ): AndroidJacocoCoverageConfiguration
    }
}
