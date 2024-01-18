/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.linter

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import tech.antibytes.gradle.quality.Configurator
import tech.antibytes.gradle.quality.QualityApiContract.LinterConfiguration.PartialConfiguration
import tech.antibytes.gradle.quality.QualityContract.Extension
import tech.antibytes.gradle.quality.config.MainConfig.ktlintVersion

internal object Spotless : Configurator() {
    private fun FormatExtension.configureFormat(configuration: PartialConfiguration) {
        target(*configuration.include.toTypedArray())
        if (configuration.exclude.isNotEmpty()) {
            targetExclude(*configuration.exclude.toTypedArray())
        }

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    private fun SpotlessExtension.configureCodeLinter(configuration: PartialConfiguration) {
        kotlin {
            configureFormat(configuration)
            ktlint(ktlintVersion).apply {
                if (configuration.disabledRules.isNotEmpty()) {
                    this.editorConfigOverride(configuration.disabledRules)
                }
            }
        }
    }

    private fun SpotlessExtension.configureGradleLinter(configuration: PartialConfiguration) {
        kotlinGradle {
            configureFormat(configuration)
            ktlint(ktlintVersion).apply {
                if (configuration.disabledRules.isNotEmpty()) {
                    this.editorConfigOverride(configuration.disabledRules)
                }
            }
        }
    }

    private fun SpotlessExtension.configureMiscellaneousLinter(configuration: PartialConfiguration) {
        format("misc") {
            configureFormat(configuration)
        }
    }

    override fun configure(
        project: Project,
        configuration: Extension,
    ) {
        configuration.linter.orNull.applyIfNotNull { linterConfiguration ->
            project.plugins.apply("com.diffplug.spotless")

            project.extensions.configure(SpotlessExtension::class.java) {
                configureCodeLinter(linterConfiguration.code)
                configureGradleLinter(linterConfiguration.gradle)
                configureMiscellaneousLinter(linterConfiguration.misc)
            }
        }
    }
}
