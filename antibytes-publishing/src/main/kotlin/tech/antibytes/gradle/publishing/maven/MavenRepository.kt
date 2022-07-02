/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.maven

import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.publisher.PublisherContract

internal object MavenRepository : PublisherContract.MavenRepository {
    private fun useCredentials(
        configuration: PublishingApiContract.RepositoryConfiguration,
        dryRun: Boolean,
    ): Boolean {
        return configuration is PublishingApiContract.MavenRepositoryConfiguration && !dryRun
    }

    private fun getUrl(
        project: Project,
        configuration: PublishingApiContract.RepositoryConfiguration,
        dryRun: Boolean,
    ): String {
        val localBasePath = "file://${project.rootProject.buildDir.absolutePath}/${configuration.name}"

        return when {
            configuration is PublishingApiContract.GitRepositoryConfiguration -> "$localBasePath/${configuration.gitWorkDirectory}"
            dryRun -> "$localBasePath/dryRun"
            else -> configuration.url
        }
    }

    private fun setRepository(
        project: Project,
        repository: MavenArtifactRepository,
        configuration: PublishingApiContract.RepositoryConfiguration,
        dryRun: Boolean,
    ) {
        repository.name = configuration.name.capitalize()
        repository.setUrl(
            getUrl(
                project,
                configuration,
                dryRun,
            ),
        )

        if (useCredentials(configuration, dryRun)) {
            repository.credentials {
                username = configuration.username
                password = configuration.password
            }
        }
    }

    override fun configure(
        project: Project,
        configuration: PublishingApiContract.RepositoryConfiguration,
        dryRun: Boolean,
    ) {
        project.extensions.configure(PublishingExtension::class.java) {
            repositories {
                repositories.maven {
                    setRepository(
                        project,
                        this,
                        configuration,
                        dryRun,
                    )
                }
            }
        }
    }
}
