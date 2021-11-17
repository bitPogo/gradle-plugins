/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.file.ConfigurableFileTree
import java.io.File
import java.math.BigDecimal

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
        BUNDLE
    }

    enum class JacocoCounter {
        LINE,
        BRANCH,
        CLASS,
        INSTRUCTION,
        METHOD
    }

    enum class JacocoMeasurement(val value: String) {
        COVERED_RATIO("COVEREDRATIO"),
        COVERED_COUNT("COVEREDCOUNT"),
        MISSED_RATIO("MISSEDRATIO"),
        MISSED_COUNT("MISSEDCOUNT"),
        TOTAL_COUNT("TOTALCOUNT")
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

    interface JacocoCoverageConfiguration : CoverageConfiguration, JacocoAgent {
        val reportSettings: JacocoReporterSettings
        var testDependencies: Set<String>
        var classPattern: Set<String>
        var classFilter: Set<String>
        var sources: Set<File>
        var additionalSources: Set<ConfigurableFileTree>
        var additionalClasses: Set<ConfigurableFileTree>
        var verificationRules: Set<JacocoVerificationRule>
    }

    interface AndroidJacocoCoverageConfiguration : JacocoCoverageConfiguration {
        var instrumentedTestDependencies: Set<String>
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
}
