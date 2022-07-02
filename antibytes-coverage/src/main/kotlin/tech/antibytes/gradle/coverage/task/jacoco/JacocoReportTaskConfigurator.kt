/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoReport
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.task.TaskContract

internal object JacocoReportTaskConfigurator : TaskContract.ReportTaskConfigurator, JacocoTaskBase() {
    private fun addReportTask(
        project: Project,
        contextId: String,
        dependencies: Set<Task>,
        executionFiles: Set<String>,
        configuration: CoverageApiContract.JacocoCoverageConfiguration,
    ): JacocoReport {
        return project.tasks.create(
            "${contextId}Coverage",
            JacocoReport::class.java,
        ) {
            this.group = "Verification"
            this.description = "Generate coverage reports for ${contextId.capitalize()}."
            this.setDependsOn(dependencies)

            configureJacocoCoverageBase(
                project,
                this,
                configuration,
                executionFiles,
            )

            configureOutput(
                project,
                contextId,
                configuration.reportSettings,
                this,
            )
        }
    }

    private fun determineProjectTestDependencies(
        project: Project,
        vararg testTaskNames: Set<String>,
    ): Set<Task> {
        val dependencies: MutableSet<Task> = mutableSetOf()
        testTaskNames.forEach { container ->
            container
                .map { name -> project.tasks.findByName(name) }
                .filterIsInstance<Task>()
                .map { task -> dependencies.add(task) }
        }

        return dependencies
    }

    override fun configure(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.CoverageConfiguration,
    ): Task {
        configuration as CoverageApiContract.JacocoCoverageConfiguration

        val testDependencies = if (configuration is CoverageApiContract.AndroidJacocoCoverageConfiguration) {
            determineProjectTestDependencies(
                project,
                configuration.testDependencies,
                configuration.instrumentedTestDependencies,
            )
        } else {
            determineProjectTestDependencies(project, configuration.testDependencies)
        }

        return addReportTask(
            project,
            contextId,
            testDependencies,
            determineExecutionsFiles(configuration),
            configuration,
        )
    }
}
