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
        val useHtml: Boolean
        val useCsv: Boolean
        val useXml: Boolean
    }

    interface VerificationRule

    enum class JacocoElement {
        CLASS,
        METHOD
    }

    enum class JacocoCounter {
        LINE,
        BRANCH,
        CLASS,
        INSTRUCTION,
        METHOD
    }

    enum class JacocoMeasurement(val value: String) {
        COVERED_RATION("COVEREDRATIO"),
        COVERED_COUNT("COVEREDCOUNT"),
        MISSED_RATIO("MISSEDRATIO"),
        MISSED_COUNT("MISSEDCOUNT"),
        TOTAL_COUNT("TOTALCOUNT")
    }

    interface JacocoVerificationRule : VerificationRule, JacocoAgent {
        val element: JacocoElement?
        val enable: Boolean?
        val counter: JacocoCounter?
        val measurement: JacocoMeasurement?
        val minimum: BigDecimal?
        val maximum: BigDecimal?
        val includes: Set<String>?
        val excludes: Set<String>?
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
        var violationRules: Set<VerificationRule>
    }

    interface AndroidJacocoCoverageConfiguration : JacocoCoverageConfiguration {
        var instrumentedTestDependencies: Set<String>
        var variant: String
        var flavour: String
    }
}
