/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingContract.PublisherController
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension

internal object PublisherRootProjectController : PublisherController, SharedPublisherFunctions() {
    private fun Project.findTasks(
        repositoryName: String,
        platforms: Set<String>,
    ): List<Task> {
        return subprojects
            .asSequence()
            .map { subproject ->
                subproject.findPublishingTasks(repositoryName, platforms)
            }.flatten()
            .filterNotNull()
            .toList()
    }

    override fun Project.wireDependencies(
        cloneTask: Task?,
        pushTask: Task?,
        publishingTask: Task,
        repositoryName: String,
        platforms: Set<String>,
    ) {
        val mavenTasks = findTasks(repositoryName, platforms)

        wireDependencies(
            cloneTask = cloneTask,
            mavenTasks = mavenTasks,
            pushTask = pushTask,
            publishingTask = publishingTask,
        )
    }

    override fun configure(
        project: Project,
        version: String,
        documentation: Task?,
        extension: PublishingPluginExtension,
    ) {
        extension.repositories.get().forEach { repository ->
            project.configurePublishingTasks(
                version = version,
                repository = repository,
                extension = extension,
            )
        }
    }
}
