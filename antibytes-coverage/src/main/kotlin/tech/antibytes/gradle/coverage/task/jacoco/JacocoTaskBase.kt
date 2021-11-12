/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReportBase
import tech.antibytes.gradle.coverage.CoverageApiContract

internal abstract class JacocoTaskBase {
    protected fun setupJacocoCoverageBase(
        configuration: CoverageApiContract.JacocoCoverageConfiguration,
        executionFiles: Set<String>,
        project: Project,
        task: JacocoReportBase
    ) {
        task.sourceDirectories.setFrom(configuration.sources)
        task.classDirectories.setFrom(
            project.fileTree(project.projectDir) {
                setIncludes(configuration.classPattern)
                setExcludes(configuration.classFilter)
            }
        )

        task.additionalSourceDirs.setFrom(configuration.additionalSources)
        task.additionalClassDirs.setFrom(configuration.additionalClasses)
        task.executionData.setFrom(
            project.fileTree(project.buildDir) {
                setIncludes(executionFiles)
            }
        )
    }
}
