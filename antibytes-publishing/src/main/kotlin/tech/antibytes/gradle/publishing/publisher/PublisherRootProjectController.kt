/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.PublishingContract.PublisherController
import tech.antibytes.gradle.publishing.git.GitRepository

internal object PublisherRootProjectController : PublisherController, SharedPublisherFunctions() {
    private fun Project.findTasks(
        repositoryName: String,
        platforms: Set<String>,
    ): List<Task> {
        return subprojects
            .asSequence()
            .map { subproject ->
                subproject.findTasksInSubproject(repositoryName, platforms)
            }.flatten()
            .filterNotNull()
            .toList()
    }

    private fun Project.wireDependencies(
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

    private fun Project.configurePublishingVariants(
        version: String,
        cloneTask: Task?,
        variants: Map<String, Set<String>>,
        repository: RepositoryConfiguration<out Any>,
        extension: PublishingPluginExtension,
    ) {
        variants.forEach { (variantName, dependencies) ->
            val pushTask = GitRepository.configurePushTask(
                project = project,
                configuration = repository,
                version = version,
                dryRun = extension.dryRun.get(),
                publishingId = variantName,
            )

            val publishTask = addPublishingTask(
                project = project,
                configuration = repository,
                publishingId = variantName
            )

            project.wireDependencies(
                cloneTask = cloneTask,
                pushTask = pushTask,
                publishingTask = publishTask,
                repositoryName = repository.name,
                platforms = dependencies
            )
        }
    }

    private fun Project.configurePublishingTasks(
        version: String,
        repository: RepositoryConfiguration<out Any>,
        extension: PublishingPluginExtension,
    ) {
        val cloneTask = GitRepository.configureCloneTask(project, repository)
        val publishingVariants = resolvePublishingTasks(extension)
        configurePublishingVariants(
            extension = extension,
            cloneTask = cloneTask,
            version = version,
            variants = publishingVariants,
            repository = repository,
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
