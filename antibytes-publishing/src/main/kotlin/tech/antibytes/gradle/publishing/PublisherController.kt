/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.git.GitRepository
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRegistry

internal object PublisherController : PublishingContract.PublisherController {
    private fun isApplicable(
        projectName: String,
        configuration: PublishingContract.PublishingPluginConfiguration
    ): Boolean {
        return configuration.registryConfiguration.get().isNotEmpty() &&
            !configuration.excludeProjects.get().contains(projectName)
    }

    private fun addPublishingTask(
        project: Project,
        configuration: PublishingApiContract.RegistryConfiguration
    ) {
        project.tasks.create("publish${configuration.name.capitalize()}") {
            group = "Publishing"
            description = "Publish ${configuration.name.capitalize()}"
        }
    }

    private fun wireDependencies(
        project: Project,
        configuration: PublishingApiContract.RegistryConfiguration
    ) {
        project.tasks.named("publishAllPublicationsTo${configuration.name.capitalize()}Repository") {
            dependsOn("clone${configuration.name.capitalize()}")
        }

        project.tasks.named("push${configuration.name.capitalize()}") {
            dependsOn("publishAllPublicationsTo${configuration.name.capitalize()}Repository")
        }

        project.tasks.named("publish${configuration.name.capitalize()}") {
            dependsOn("push${configuration.name.capitalize()}")
        }
    }

    override fun configure(
        project: Project,
        configuration: PublishingContract.PublishingPluginConfiguration
    ) {
        project.afterEvaluate {
            if (isApplicable(project.name, configuration)) {
                val dryRun = configuration.dryRun.get()
                val registryConfigurations = configuration.registryConfiguration.get()
                val packageConfiguration = configuration.packageConfiguration.get()

                val version = Versioning.versionName(
                    project,
                    configuration.versioning.get()
                )

                MavenPublisher.configure(
                    project,
                    packageConfiguration,
                    version
                )

                registryConfigurations.forEach { configuration ->
                    MavenRegistry.configure(project, configuration, dryRun)

                    GitRepository.configureCloneTask(project, configuration)
                    GitRepository.configurePushTask(project, configuration, version, dryRun)

                    addPublishingTask(project, configuration)
                    wireDependencies(project, configuration)
                }
            }
        }
    }
}
