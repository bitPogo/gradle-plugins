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

internal abstract class SharedPublisherFunctions {
    private fun resolveDescription(
        repositoryName: String,
        publishingId: String
    ): String {
        return if (publishingId.isEmpty()) {
            "Publish $repositoryName"
        } else {
            "Publish $publishingId to $repositoryName"
        }
    }

    protected fun addPublishingTask(
        project: Project,
        configuration: RepositoryConfiguration<out Any>,
        publishingId: String = ""
    ): Task {
        val repositoryName = configuration.name.capitalize()
        val taskNameInfix = publishingId.capitalize()

        return project.tasks.create("publish$taskNameInfix$repositoryName") {
            group = "Publishing"
            description = resolveDescription(
                repositoryName = repositoryName,
                publishingId = taskNameInfix
            )
        }
    }

    protected fun Project.findTasksInSubproject(
        repositoryName: String,
        platforms: Set<String>,
    ): List<Task?> {
        return platforms.map { platform ->
            tasks.findByName(
                "publish${platform.capitalize()}PublicationsTo${repositoryName.capitalize()}Repository",
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
            mavenTasks = mavenTasks
        )

        val pushOrMaven = determinePublishingDependencyTask(
            mavenTasks = mavenTasks,
            pushTask = pushTask
        )

        publishingTask.dependsOn(pushOrMaven)
        publishingTask.mustRunAfter(pushOrMaven)
    }

    protected fun resolvePublishingTasks(extension: PublishingPluginExtension): Map<String, Set<String>> {
        return mutableMapOf(
            "" to setOf("all"),
        ).apply {
            putAll(extension.additionalPublishingTasks.get())
        }
    }
}
