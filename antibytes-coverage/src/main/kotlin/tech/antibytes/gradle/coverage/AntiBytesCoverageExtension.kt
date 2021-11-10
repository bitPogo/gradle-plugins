/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.configuration.DefaultCoverageProvider

abstract class AntiBytesCoverageExtension(project: Project) : CoverageContract.Extension {
    override val coverageConfigurations: MutableMap<String, CoverageConfiguration> = DefaultCoverageProvider.createDefaultCoverageConfiguration(project)
    override var jacocoVersion: String = "0.8.7"
}
