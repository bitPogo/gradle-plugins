/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.coverage.AntiBytesCoverageExtension
import tech.antibytes.gradle.coverage.CoverageApiContract.AndroidJacocoCoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.CoverageError
import tech.antibytes.gradle.coverage.configuration.PlatformContextResolver.isKmp
import tech.antibytes.gradle.coverage.isRoot
import tech.antibytes.gradle.coverage.task.extension.AndroidExtensionConfigurator
import tech.antibytes.gradle.coverage.task.extension.JacocoExtensionConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoAggregationReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoVerificationTaskConfigurator

internal object TaskController : CoverageContract.TaskController {
    private fun configureJacocoTask(
        project: Project,
        contextId: String,
        configuration: JacocoCoverageConfiguration
    ): Pair<Task, Task?> {
        return Pair(
            JacocoReportTaskConfigurator.configure(project, contextId, configuration),
            JacocoVerificationTaskConfigurator.configure(project, contextId, configuration)
        )
    }

    private fun configureJacocoExtensions(
        project: Project,
        contextId: String,
        extension: AntiBytesCoverageExtension,
    ) {
        JacocoExtensionConfigurator.configure(project, extension)
        val configuration = extension.configurations[contextId]

        if (configuration is AndroidJacocoCoverageConfiguration) {
            AndroidExtensionConfigurator.configure(project)
        }
    }

    private fun addCoverageTask(
        project: Project,
        dependencies: Set<Task>
    ) {
        project.tasks.create("multiplatformCoverage") {
            group = "Verification"
            description = "Generate a coverage reports for all platforms of multiplatform projects."

            dependsOn(dependencies)
        }
    }

    private fun addVerificationTask(
        project: Project,
        dependencies: List<Task>
    ) {
        project.tasks.create("multiplatformCoverageVerification") {
            group = "Verification"
            description = "Verifies the coverage for all platforms of multiplatform projects."

            dependsOn(dependencies)
        }
    }

    private fun addMultiplatformTasks(
        project: Project,
        dependencies: Map<Task, Task?>
    ) {
        addCoverageTask(project, dependencies.keys)

        val verificationTasks: List<Task> = dependencies.values.filterIsInstance<Task>()

        if (verificationTasks.isNotEmpty()) {
            addVerificationTask(project, verificationTasks)
        }
    }

    private fun configureModule(
        project: Project,
        extension: AntiBytesCoverageExtension
    ) {
        val tasks = mutableMapOf<Task, Task?>()
        extension.configurations.forEach { (contextId, configuration) ->
            val (reporter, verification) = when (configuration) {
                is JacocoCoverageConfiguration -> configureJacocoTask(project, contextId, configuration).also {
                    configureJacocoExtensions(project, contextId, extension)
                }
                else -> throw CoverageError.UnknownPlatformConfiguration()
            }

            tasks[reporter] = verification
        }

        if (isKmp(project)) {
            addMultiplatformTasks(project, tasks)
        }
    }

    private fun configureJacocoAggregation(
        project: Project,
        contextId: String,
        configuration: JacocoAggregationConfiguration
    ) {
        JacocoAggregationReportTaskConfigurator.configure(project, contextId, configuration)
    }

    private fun configureAggregation(
        project: Project,
        extension: AntiBytesCoverageExtension
    ) {
        extension.configurations.forEach { (contextId, configuration) ->
            when (configuration) {
                is JacocoAggregationConfiguration -> configureJacocoAggregation(project, contextId, configuration).also {
                    JacocoExtensionConfigurator.configure(project, extension)
                }
                else -> throw CoverageError.UnknownPlatformConfiguration()
            }
        }
    }

    override fun configure(
        project: Project,
        extension: AntiBytesCoverageExtension
    ) {
        if (project.isRoot()) {
            configureAggregation(project, extension)
        } else {
            configureModule(project, extension)
        }
    }
}
