/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.JACOCO_VERSION
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider

abstract class AntiBytesCoverageExtension(project: Project) : CoverageContract.Extension {
    override val configurations: MutableMap<String, CoverageConfiguration> = if (project.isRoot()) {
        mutableMapOf()
    } else {
        DefaultConfigurationProvider.createDefaultCoverageConfiguration(project)
    }
    override var jacocoVersion: String = JACOCO_VERSION
    override var appendKmpJvmTask: Boolean = true
}
