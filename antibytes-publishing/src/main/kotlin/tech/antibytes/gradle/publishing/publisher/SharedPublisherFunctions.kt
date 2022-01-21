/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract

internal abstract class SharedPublisherFunctions {
    protected fun addPublishingTask(
        project: Project,
        configuration: PublishingApiContract.RepositoryConfiguration
    ): Task {
        return project.tasks.create("publish${configuration.name.capitalize()}") {
            group = "Publishing"
            description = "Publish ${configuration.name.capitalize()}"
        }
    }

    protected fun wireDependencies(
        cloneTask: Task?,
        mavenTasks: List<Task>,
        pushTask: Task?,
        publishingTask: Task,
    ) {
        if (pushTask is Task) {
            mavenTasks.forEach { task ->
                task.dependsOn(cloneTask)
            }
            pushTask.dependsOn(mavenTasks)
            publishingTask.dependsOn(pushTask)
        } else {
            publishingTask.dependsOn(mavenTasks)
        }
    }
}
