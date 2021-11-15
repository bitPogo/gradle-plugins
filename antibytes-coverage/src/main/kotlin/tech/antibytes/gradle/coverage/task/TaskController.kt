/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.coverage.AntiBytesCoverageExtension
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.CoverageError
import tech.antibytes.gradle.coverage.configuration.PlatformContextResolver.isKmp
import tech.antibytes.gradle.coverage.task.extension.AndroidExtensionConfigurator
import tech.antibytes.gradle.coverage.task.extension.JacocoExtensionConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoReportTaskConfigurator
import tech.antibytes.gradle.coverage.task.jacoco.JacocoVerificationTaskConfigurator

internal object TaskController : CoverageContract.TaskController {
    private fun configureJacocoTask(
        project: Project,
        contextName: String,
        configuration: JacocoCoverageConfiguration
    ): Pair<Task, Task?> {
        return Pair(
            JacocoReportTaskConfigurator.configure(project, contextName, configuration),
            JacocoVerificationTaskConfigurator.configure(project, contextName, configuration)
        )
    }

    private fun configureJacocoExtensions(
        project: Project,
        contextName: String,
        extension: AntiBytesCoverageExtension,
    ) {
        JacocoExtensionConfigurator.configure(project, extension)
        val configuration = extension.coverageConfigurations[contextName]

        if (configuration is CoverageApiContract.AndroidJacocoCoverageConfiguration) {
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

    override fun configure(
        project: Project,
        extension: AntiBytesCoverageExtension
    ) {
        val tasks = mutableMapOf<Task, Task?>()
        extension.coverageConfigurations.forEach { (contextName, configuration) ->
            val (reporter, verification) = when (configuration) {
                is JacocoCoverageConfiguration -> configureJacocoTask(project, contextName, configuration).also {
                    configureJacocoExtensions(project, contextName, extension)
                }
                else -> throw CoverageError.UnknownPlatformConfiguration()
            }

            tasks[reporter] = verification
        }

        if (isKmp(project)) {
            addMultiplatformTasks(project, tasks)
        }
    }
}
