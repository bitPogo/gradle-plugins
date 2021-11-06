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

    private fun addVersionTask(
        project: Project,
        configuration: PublishingApiContract.VersioningConfiguration
    ) {
        project.tasks.create("versionInfo") {
            group = "Versioning"
            description = "Displays the current version"

            doLast {
                val info = Versioning.versionInfo(project, configuration)
                println("version: ${info.name}")
                println("last tag: ${info.details.lastTag}")
                println("distance from last tag: ${info.details.commitDistance}")
                println("clean: ${info.details.isCleanTag}")
                println("hash: ${info.details.gitHashFull}")
            }
        }
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
                val versioningConfiguration = configuration.versioning.get()

                val version = Versioning.versionName(
                    project,
                    versioningConfiguration
                )

                addVersionTask(project, versioningConfiguration)

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
