/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.file.ConfigurableFileTree
import java.io.File
import java.math.BigDecimal

interface AntiBytesCoverageApiContract {
    interface Agent

    interface JacocoAgent : Agent

    interface Reporter

    interface JacocoReporter : Reporter, JacocoAgent {
        val useHTML: Boolean
        val useCSV: Boolean
        val useXML: Boolean
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

    enum class JacocoMeasurement {
        LINE,
        BRANCH,
        CLASS,
        INSTRUCTION,
        METHOD
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

    interface CoverageConfiguration {
        val aggregate: Boolean
    }

    interface JacocoCoverageConfiguration : CoverageConfiguration, JacocoAgent {
        val reportSettings: JacocoReporter
        var testDependencies: Set<String>
        var classBase: Set<String>
        var classFilter: Set<String>
        var sources: Set<File>
        var additionalSources: Set<ConfigurableFileTree>
        var additionalClasses: Set<ConfigurableFileTree>
        var violationRules: Set<VerificationRule>
    }

    interface AndroidJacocoCoverageConfiguration : JacocoCoverageConfiguration {
        var instrumentedTests: Set<String>
        var variant: String
        var flavour: String
    }
}
