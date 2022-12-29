/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.config.MainConfig
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.util.isRoot

abstract class AntibytesCoveragePluginExtension(project: Project) : CoverageContract.CoveragePluginExtension {
    init {
        configurations.convention(determineDefaultConfiguration(project))
        jacocoVersion.convention(MainConfig.jacoco)
        appendKmpJvmTask.convention(true)
    }

    private fun determineDefaultConfiguration(project: Project): MutableMap<String, CoverageConfiguration> {
        return if (project.isRoot()) {
            mutableMapOf()
        } else {
            DefaultConfigurationProvider.createDefaultCoverageConfiguration(project)
        }
    }
}
