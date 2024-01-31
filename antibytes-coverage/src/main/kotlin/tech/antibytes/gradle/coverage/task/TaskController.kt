/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.task

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.coverage.AntibytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.CoverageApiContract.AndroidJacocoCoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.CoverageError
import tech.antibytes.gradle.coverage.task.extension.AndroidExtensionConfigurator
import tech.antibytes.gradle.coverage.task.extension.JacocoExtensionConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoAggregationReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoAggregationVerificationTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoVerificationTaskConfigurator
import tech.antibytes.gradle.util.isKmp
import tech.antibytes.gradle.util.isRoot

internal class TaskController(
    private val jacocoExtensionConfigurator: TaskContract.JacocoExtensionConfigurator = JacocoExtensionConfigurator(),
    private val androidExtensionConfigurator: TaskContract.AndroidExtensionConfigurator = AndroidExtensionConfigurator(),
    private val jacocoReporterTaskConfigurator: TaskContract.ReportTaskConfigurator = JacocoReportTaskConfigurator(),
    private val jacocoVerificationTaskConfigurator: TaskContract.VerificationTaskConfigurator = JacocoVerificationTaskConfigurator(),
    private val jacocoAggregationReportTaskConfigurator: TaskContract.ReportTaskConfigurator = JacocoAggregationReportTaskConfigurator(),
    private val jacocoAggregationVerificationTaskConfigurator: TaskContract.VerificationTaskConfigurator = JacocoAggregationVerificationTaskConfigurator(),
) : CoverageContract.TaskController {
    private fun configureJacocoTask(
        project: Project,
        contextId: String,
        configuration: JacocoCoverageConfiguration,
    ): Pair<Task, Task?> {
        return Pair(
            jacocoReporterTaskConfigurator.configure(project, contextId, configuration),
            jacocoVerificationTaskConfigurator.configure(project, contextId, configuration),
        )
    }

    private fun configureAggregationJacocoTask(
        project: Project,
        contextId: String,
        configuration: JacocoAggregationConfiguration,
    ): Pair<Task, Task?> {
        return Pair(
            jacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration),
            jacocoAggregationVerificationTaskConfigurator.configure(project, contextId, configuration),
        )
    }

    private fun configureJacocoExtensions(
        project: Project,
        contextId: String,
        extension: AntibytesCoveragePluginExtension,
    ) {
        jacocoExtensionConfigurator.configure(project, extension)
        val configuration = extension.configurations.get()[contextId]

        if (configuration is AndroidJacocoCoverageConfiguration) {
            androidExtensionConfigurator.configure(project)
        }
    }

    private fun addCoverageTask(
        project: Project,
        dependencies: Set<Task>,
    ) {
        project.tasks.create("multiplatformCoverage") {
            group = "Verification"
            description = "Generate a coverage reports for all platforms of multiplatform projects."

            dependsOn(dependencies)
        }
    }

    private fun addVerificationTask(
        project: Project,
        dependencies: List<Task>,
    ) {
        project.tasks.create("multiplatformCoverageVerification") {
            group = "Verification"
            description = "Verifies the coverage for all platforms of multiplatform projects."

            dependsOn(dependencies)
        }
    }

    private fun addMultiplatformTasks(
        project: Project,
        dependencies: Map<Task, Task?>,
    ) {
        addCoverageTask(project, dependencies.keys)

        val verificationTasks: List<Task> = dependencies.values.filterIsInstance<Task>()

        if (verificationTasks.isNotEmpty()) {
            addVerificationTask(project, verificationTasks)
        }
    }

    private fun configureModule(
        project: Project,
        extension: AntibytesCoveragePluginExtension,
    ) {
        val tasks = mutableMapOf<Task, Task?>()
        extension.configurations.get().forEach { (contextId, configuration) ->
            val (reporter, verification) = when (configuration) {
                is JacocoCoverageConfiguration -> configureJacocoTask(project, contextId, configuration).also {
                    configureJacocoExtensions(project, contextId, extension)
                }
                else -> throw CoverageError.UnknownPlatformConfiguration()
            }

            tasks[reporter] = verification
        }

        if (project.isKmp()) {
            addMultiplatformTasks(project, tasks)
        }
    }

    private fun configureAggregation(
        project: Project,
        extension: AntibytesCoveragePluginExtension,
    ) {
        val tasks = mutableMapOf<Task, Task?>()
        extension.configurations.get().forEach { (contextId, configuration) ->
            val (reporter, verification) = when (configuration) {
                is JacocoAggregationConfiguration -> configureAggregationJacocoTask(project, contextId, configuration).also {
                    jacocoExtensionConfigurator.configure(project, extension)
                }
                else -> throw CoverageError.UnknownPlatformConfiguration()
            }

            tasks[reporter] = verification
        }

        if (tasks.size > 1) {
            addMultiplatformTasks(project, tasks)
        }
    }

    override fun configure(
        project: Project,
        extension: AntibytesCoveragePluginExtension,
    ) {
        if (project.isRoot()) {
            configureAggregation(project, extension)
        } else {
            configureModule(project, extension)
        }
    }
}
