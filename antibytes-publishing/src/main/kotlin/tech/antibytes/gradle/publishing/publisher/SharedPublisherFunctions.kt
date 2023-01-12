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
import tech.antibytes.gradle.publishing.git.GitRepository

internal abstract class SharedPublisherFunctions {
    private fun resolveDescription(
        repositoryName: String,
        publishingId: String,
    ): String {
        return if (publishingId.isEmpty()) {
            "Publish $repositoryName"
        } else {
            "Publish $publishingId to $repositoryName"
        }
    }

    private fun Project.addPublishingTask(
        configuration: RepositoryConfiguration<out Any>,
        publishingId: String = "",
    ): Task {
        val repositoryName = configuration.name.capitalize()
        val taskNameInfix = publishingId.capitalize()

        return tasks.create("publish$taskNameInfix$repositoryName") {
            group = "Publishing"
            description = resolveDescription(
                repositoryName = repositoryName,
                publishingId = taskNameInfix,
            )
        }
    }

    private fun Set<String>.determinePlural(): String {
        return if (this == defaultTaskDependencies) {
            "s"
        } else {
            ""
        }
    }

    protected fun Project.findPublishingTasks(
        repositoryName: String,
        platforms: Set<String>,
    ): List<Task?> {
        return platforms.map { platform ->
            tasks.findByName(
                "publish${platform.capitalize()}Publication${platforms.determinePlural()}To${repositoryName.capitalize()}Repository",
            )
        }
    }

    private fun wireGitTasksIfNeeded(
        cloneTask: Task?,
        mavenTasks: List<Task>,
        pushTask: Task?,
    ) {
        if (pushTask is Task) {
            mavenTasks.forEach { task ->
                task.dependsOn(cloneTask)
                task.mustRunAfter(cloneTask)
            }

            pushTask.dependsOn(mavenTasks)
            pushTask.mustRunAfter(mavenTasks)
        }
    }

    private fun determinePublishingDependencyTask(
        mavenTasks: List<Task>,
        pushTask: Task?,
    ): List<Task> {
        return if (pushTask is Task) {
            listOf(pushTask)
        } else {
            mavenTasks
        }
    }

    protected fun wireDependencies(
        cloneTask: Task?,
        mavenTasks: List<Task>,
        pushTask: Task?,
        publishingTask: Task,
    ) {
        wireGitTasksIfNeeded(
            cloneTask = cloneTask,
            pushTask = pushTask,
            mavenTasks = mavenTasks,
        )

        val pushOrMaven = determinePublishingDependencyTask(
            mavenTasks = mavenTasks,
            pushTask = pushTask,
        )

        publishingTask.dependsOn(pushOrMaven)
        publishingTask.mustRunAfter(pushOrMaven)
    }

    private fun resolvePublishingTasks(extension: PublishingPluginExtension): Map<String, Set<String>> {
        return mutableMapOf(
            "" to defaultTaskDependencies,
        ).apply {
            putAll(extension.additionalPublishingTasks.get())
        }
    }

    protected abstract fun Project.wireDependencies(
        cloneTask: Task?,
        pushTask: Task?,
        publishingTask: Task,
        repositoryName: String,
        platforms: Set<String>,
    )

    private fun Project.configurePublishingVariants(
        version: String,
        cloneTask: Task?,
        variants: Map<String, Set<String>>,
        repository: RepositoryConfiguration<out Any>,
        extension: PublishingPluginExtension,
    ) {
        variants.forEach { (variantName, dependencies) ->
            val pushTask = GitRepository.configurePushTask(
                project = this,
                configuration = repository,
                version = version,
                dryRun = extension.dryRun.get(),
                publishingId = variantName,
            )

            val publishTask = addPublishingTask(
                configuration = repository,
                publishingId = variantName,
            )

            wireDependencies(
                cloneTask = cloneTask,
                pushTask = pushTask,
                publishingTask = publishTask,
                repositoryName = repository.name,
                platforms = dependencies,
            )
        }
    }

    protected fun Project.configurePublishingTasks(
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

    private companion object {
        val defaultTaskDependencies = setOf("all")
    }
}
