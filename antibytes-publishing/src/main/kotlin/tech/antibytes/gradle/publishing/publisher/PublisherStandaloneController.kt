/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.Versioning
import tech.antibytes.gradle.publishing.git.GitRepository
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRegistry

internal object PublisherStandaloneController : PublishingContract.PublisherController {
    private fun isApplicable(
        configuration: PublishingContract.PublishingPluginConfiguration
    ): Boolean {
        return configuration.registryConfiguration.isNotEmpty() &&
            configuration.packageConfiguration is PublishingApiContract.PackageConfiguration
    }

    private fun wireDependencies(
        project: Project,
        cloneTask: Task?,
        pushTask: Task?,
        publishingTask: Task,
        registryName: String,
    ) {
        val mavenTask = project.tasks.findByName(
            "publishAllPublicationsTo${registryName.capitalize()}Repository"
        ) as Task

        wireDependencies(
            cloneTask = cloneTask,
            mavenTasks = listOf(mavenTask),
            pushTask = pushTask,
            publishingTask = publishingTask,
        )
    }

    override fun configure(
        project: Project,
        configuration: PublishingContract.PublishingPluginConfiguration
    ) {
        if (isApplicable(configuration)) {
            val dryRun = configuration.dryRun
            val registryConfigurations = configuration.registryConfiguration
            val packageConfiguration = configuration.packageConfiguration as PublishingApiContract.PackageConfiguration
            val versioningConfiguration = configuration.versioning

            val version = Versioning.versionName(
                project,
                versioningConfiguration
            )

            MavenPublisher.configure(
                project,
                packageConfiguration,
                version
            )

            registryConfigurations.forEach { registry ->
                MavenRegistry.configure(project, registry, dryRun)

                val clone = GitRepository.configureCloneTask(project, registry)
                val push = GitRepository.configurePushTask(project, registry, version, dryRun)
                val publish = addPublishingTask(project, registry)

                wireDependencies(
                    project = project,
                    cloneTask = clone,
                    pushTask = push,
                    publishingTask = publish,
                    registryName = registry.name,
                )
            }
        }
    }
}