/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.coverage.CoverageApiContract

internal interface TaskContract {
    fun interface AndroidExtensionConfigurator {
        fun configure(project: Project, configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration)
    }

    fun interface ReportTaskConfigurator {
        fun configure(
            project: Project,
            contextName: String,
            configuration: CoverageApiContract.CoverageConfiguration
        ): Task
    }

    fun interface VerificationTaskConfigurator {
        fun configure(
            project: Project,
            contextName: String,
            configuration: CoverageApiContract.CoverageConfiguration
        ): Task?
    }
}
