/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract.PublisherController
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRepository

internal object PublisherSubProjectController : PublisherController {
    private fun Project.determineRepositories(
        extension: PublishingPluginExtension
    ): Set<RepositoryConfiguration<out Any>> {
        val repositories = extension.repositories.get()

        return if (repositories.isEmpty()) {
            rootProject.extensions.getByType(PublishingPluginExtension::class.java).repositories.get()
        } else {
            repositories
        }
    }

    private fun PublishingPluginExtension.isApplicable(
        repositories: Set<RepositoryConfiguration<out Any>>
    ): Boolean {
        return repositories.isNotEmpty() &&
            packaging.orNull is PublishingApiContract.PackageConfiguration
    }

    override fun configure(
        project: Project,
        version: String,
        documentation: Task?,
        extension: PublishingPluginExtension,
    ) {
        val repositories = project.determineRepositories(extension)

        if (extension.isApplicable(repositories)) {
            MavenPublisher.configure(
                project = project,
                configuration = extension.packaging.get(),
                version = version,
                docs = documentation,
            )

            repositories.forEach { repository ->
                MavenRepository.configure(
                    project,
                    repository,
                    extension.dryRun.get(),
                )
            }
        }
    }
}
