/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.config.MainConfig
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.util.isRoot

abstract class AntibytesCoveragePluginExtension internal constructor(
    project: Project,
    private val defaultConfiguration: DefaultConfigurationProvider,
) : CoverageContract.CoveragePluginExtension {
    constructor(project: Project) : this(project, DefaultConfigurationProvider())

    init {
        configurations.convention(determineDefaultConfiguration(project))
        jacocoVersion.convention(MainConfig.jacoco)
        appendKmpJvmTask.convention(true)
    }

    private fun determineDefaultConfiguration(project: Project): MutableMap<String, CoverageConfiguration> {
        return if (project.isRoot()) {
            mutableMapOf()
        } else {
            defaultConfiguration.createDefaultCoverageConfiguration(project)
        }
    }
}
