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
import java.io.File

internal object JacocoReportTaskConfigurator : TaskContract.ReportTaskConfigurator, JacocoTaskBase() {
    private fun setupReport(
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

    private fun configureReport(
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

            setupJacocoCoverageBase(
                configuration,
                executionFiles,
                project,
                this
            )

            setupReport(
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

    private fun determineJvmExecutionFiles(
        contextName: Set<String>
    ): Set<String> = contextName.map { name -> "jacoco${File.separator}$name.exec" }.toSet()

    private fun resolveAndroidExecutionsFiles(
        configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration
    ): Set<String> {
        val execs = determineJvmExecutionFiles(configuration.testDependencies).toMutableSet()
        val infix = "${configuration.flavour}${configuration.variant.capitalize()}AndroidTest".decapitalize()

        execs.add("outputs${File.separator}code_coverage${File.separator}$infix${File.separator}**${File.separator}*coverage.ec")
        execs.add("jacoco${File.separator}jacoco.exec")

        return execs
    }

    private fun configureJvmReport(
        project: Project,
        contextName: String,
        configuration: CoverageApiContract.JacocoCoverageConfiguration
    ): JacocoReport {
        return configureReport(
            project,
            contextName,
            determineTestDependencies(project, configuration.testDependencies),
            determineJvmExecutionFiles(configuration.testDependencies),
            configuration
        )
    }

    private fun configureAndroidReport(
        project: Project,
        contextName: String,
        configuration: CoverageApiContract.AndroidJacocoCoverageConfiguration
    ): JacocoReport {
        return configureReport(
            project,
            contextName,
            determineTestDependencies(
                project,
                configuration.testDependencies,
                configuration.instrumentedTestDependencies
            ),
            resolveAndroidExecutionsFiles(configuration),
            configuration
        )
    }

    override fun configure(
        project: Project,
        contextName: String,
        configuration: CoverageApiContract.CoverageConfiguration
    ): TaskContract.CoverageTasks {
        return if (configuration is CoverageApiContract.AndroidJacocoCoverageConfiguration) {
            TaskContract.CoverageTasks(
                configureAndroidReport(
                    project,
                    contextName,
                    configuration
                )
            )
        } else {
            TaskContract.CoverageTasks(
                configureJvmReport(
                    project,
                    contextName,
                    configuration as CoverageApiContract.JacocoCoverageConfiguration
                )
            )
        }
    }
}
