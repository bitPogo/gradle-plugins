/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
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
    private fun configureOutput(
        project: Project,
        contextName: String,
        reportSettings: CoverageApiContract.JacocoReporterSettings,
        task: JacocoReport
    ) {
        task.reports {
            html.required.set(reportSettings.useHtml)
            xml.required.set(reportSettings.useXml)
            csv.required.set(reportSettings.useCsv)

            html.outputLocation.set(
                project.layout.buildDirectory.dir(
                    "reports/jacoco/$contextName/${project.name}"
                ).get().asFile
            )
            csv.outputLocation.set(
                project.layout.buildDirectory.file(
                    "reports/jacoco/$contextName/${project.name}.csv"
                ).get().asFile
            )
            xml.outputLocation.set(
                project.layout.buildDirectory.file(
                    "reports/jacoco/$contextName/${project.name}.xml"
                ).get().asFile
            )
        }
    }

    private fun addReportTask(
        project: Project,
        contextName: String,
        dependencies: Set<Task>,
        executionFiles: Set<String>,
        configuration: CoverageApiContract.JacocoCoverageConfiguration
    ): JacocoReport {
        return project.tasks.create(
            "${contextName}Coverage",
            JacocoReport::class.java
        ) {
            this.group = "Verification"
            this.description = "Generate coverage reports for ${contextName.capitalize()}."
            this.setDependsOn(dependencies)

            configureJacocoCoverageBase(
                configuration,
                executionFiles,
                project,
                this
            )

            configureOutput(
                project,
                contextName,
                configuration.reportSettings,
                this
            )
        }
    }

    private fun determineTestDependencies(
        project: Project,
        vararg testTaskNames: Set<String>
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
        contextName: String,
        configuration: CoverageApiContract.CoverageConfiguration
    ): Task {
        configuration as CoverageApiContract.JacocoCoverageConfiguration

        val testDependencies = if (configuration is CoverageApiContract.AndroidJacocoCoverageConfiguration) {
            determineTestDependencies(
                project,
                configuration.testDependencies,
                configuration.instrumentedTestDependencies
            )
        } else {
            determineTestDependencies(project, configuration.testDependencies)
        }

        return addReportTask(
            project,
            contextName,
            testDependencies,
            determineExecutionsFiles(configuration),
            configuration
        )
    }
}
