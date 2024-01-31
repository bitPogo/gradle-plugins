/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingContract.PublisherController
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRepository

internal object PublisherStandaloneController : PublisherController, SharedPublisherFunctions() {
    private fun isApplicable(
        extension: PublishingPluginExtension,
    ): Boolean {
        return extension.repositories.get().isNotEmpty() &&
            extension.packaging.orNull is PackageConfiguration
    }

    override fun Project.wireDependencies(
        cloneTask: Task?,
        pushTask: Task?,
        publishingTask: Task,
        repositoryName: String,
        platforms: Set<String>,
    ) {
        val mavenTasks = findPublishingTasks(
            repositoryName = repositoryName,
            platforms = platforms,
        ).filterNotNull()

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
        if (isApplicable(extension)) {
            val dryRun = extension.dryRun.get()
            val repositories = extension.repositories.get()
            val packages = extension.packaging.get()

            MavenPublisher.configure(
                project = project,
                configuration = packages,
                docs = documentation,
                version = version,
            )

            repositories.forEach { repository ->
                MavenRepository.configure(project, repository, dryRun)

                project.configurePublishingTasks(
                    version = version,
                    repository = repository,
                    extension = extension,
                )
            }
        }
    }
}
