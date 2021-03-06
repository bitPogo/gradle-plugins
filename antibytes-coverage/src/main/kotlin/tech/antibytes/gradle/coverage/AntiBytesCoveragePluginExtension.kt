/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.JACOCO_VERSION
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.util.isRoot

abstract class AntiBytesCoveragePluginExtension(project: Project) : CoverageContract.CoveragePluginExtension {
    override val configurations: MutableMap<String, CoverageConfiguration> = if (project.isRoot()) {
        mutableMapOf()
    } else {
        DefaultConfigurationProvider.createDefaultCoverageConfiguration(project)
    }
    override var jacocoVersion: String = JACOCO_VERSION
    override var appendKmpJvmTask: Boolean = true
}
