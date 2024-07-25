/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.quality.QualityContract.Companion.EXTENSION_ID
import tech.antibytes.gradle.quality.analysis.Detekt
import tech.antibytes.gradle.quality.gate.Sonarqube
import tech.antibytes.gradle.quality.linter.Spotless
import tech.antibytes.gradle.quality.stableapi.StableApi

class AntibytesQualityLocal : Plugin<Project> {
    private val phases = setOf(
        Spotless,
        Detekt,
        Sonarqube,
        StableApi,
    )

    private fun Project.applyPlugin(extension: AntibytesQualityExtension) {
        afterEvaluate {
            phases.forEach { configurator ->
                configurator.configure(this, extension)
            }
        }
    }

    override fun apply(target: Project) {
        val extension = target.extensions.create(
            EXTENSION_ID,
            AntibytesQualityExtension::class.java,
        )

        target.applyPlugin(extension)
    }
}
