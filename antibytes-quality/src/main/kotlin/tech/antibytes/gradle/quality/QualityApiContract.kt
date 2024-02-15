/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality

import java.io.File
import org.gradle.api.file.ConfigurableFileCollection

interface QualityApiContract {
    interface LinterConfiguration {
        interface PartialConfiguration {
            val include: Set<String>
            val exclude: Set<String>
            val rules: Map<String, String>
        }

        val code: PartialConfiguration
        val gradle: PartialConfiguration
        val misc: PartialConfiguration
    }

    interface CodeAnalysisConfiguration {
        interface Report {
            val html: Boolean
            val xml: Boolean
            val txt: Boolean
            val sarif: Boolean
        }

        val jvmVersion: String
        val autoCorrection: Boolean
        val exclude: Set<String>
        val excludeBaseline: Set<String>
        val reports: Report
        val configurationFiles: ConfigurableFileCollection
        val baselineFile: File
        val sourceFiles: ConfigurableFileCollection
    }

    interface QualityGateConfiguration {
        val projectKey: String
        val organization: String
        val host: String
        val encoding: String
        val jacoco: String
        val junit: String
        val detekt: String
        val exclude: Set<String>
    }

    interface StableApiConfiguration {
        val excludePackages: Set<String>
        val excludeProjects: Set<String>
        val excludeClasses: Set<String>
        val nonPublicMarkers: Set<String>
    }
}
