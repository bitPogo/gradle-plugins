/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.task

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.coverage.AntibytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.CoverageApiContract

internal interface TaskContract {
    fun interface JacocoExtensionConfigurator {
        fun configure(project: Project, configuration: AntibytesCoveragePluginExtension)
    }

    fun interface AndroidExtensionConfigurator {
        fun configure(project: Project)
    }

    fun interface ReportTaskConfigurator {
        fun configure(
            project: Project,
            contextId: String,
            configuration: CoverageApiContract.CoverageConfiguration,
        ): Task
    }

    fun interface VerificationTaskConfigurator {
        fun configure(
            project: Project,
            contextId: String,
            configuration: CoverageApiContract.CoverageConfiguration,
        ): Task?
    }
}
