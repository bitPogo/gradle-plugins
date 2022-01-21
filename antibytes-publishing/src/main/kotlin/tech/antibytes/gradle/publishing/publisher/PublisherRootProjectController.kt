/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.Versioning
import tech.antibytes.gradle.publishing.git.GitRepository

internal object PublisherRootProjectController : PublishingContract.PublisherController, SharedPublisherFunctions() {
    private fun wireDependencies(
        project: Project,
        cloneTask: Task?,
        pushTask: Task?,
        publishingTask: Task,
        registryName: String,
    ) {
        val mavenTasks = mutableListOf<Task>()
        project.subprojects.forEach { subproject ->
            subproject.tasks.findByName(
                "publishAllPublicationsTo${registryName.capitalize()}Repository"
            ).also { task ->
                if (task is Task) {
                    mavenTasks.add(task)
                }
            }
        }

        wireDependencies(
            cloneTask = cloneTask,
            mavenTasks = mavenTasks,
            pushTask = pushTask,
            publishingTask = publishingTask,
        )
    }

    override fun configure(
        project: Project,
        extension: PublishingContract.PublishingPluginExtension
    ) {
        if (extension.repositoryConfiguration.isNotEmpty()) {
            val version = Versioning.versionName(
                project,
                extension.versioning
            )

            extension.repositoryConfiguration.forEach { registry ->
                val cloneTask = GitRepository.configureCloneTask(project, registry)
                val pushTask = GitRepository.configurePushTask(
                    project,
                    registry,
                    version,
                    extension.dryRun
                )

                val publishTask = addPublishingTask(project, registry)

                wireDependencies(
                    project,
                    cloneTask = cloneTask,
                    pushTask = pushTask,
                    publishingTask = publishTask,
                    registryName = registry.name
                )
            }
        }
    }
}
