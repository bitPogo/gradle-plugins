/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import java.util.UUID

internal object MavenRegistry : PublishingContract.MavenRegistry {
    private fun setRepositories(
        project: Project,
        repositories: RepositoryHandler,
        configurations: List<PublishingApiContract.RegistryConfiguration>,
        dryRun: Boolean
    ) {
        configurations.forEach { configuration ->
            repositories.maven {
                setRepository(
                    project,
                    this,
                    configuration,
                    dryRun
                )
            }
        }
    }

    private fun useCredentials(
        configuration: PublishingApiContract.RegistryConfiguration,
        dryRun: Boolean
    ): Boolean {
        return configuration.username is String &&
            configuration.password is String &&
            !configuration.useGit &&
            !dryRun
    }

    private fun getUrl(
        project: Project,
        configuration: PublishingApiContract.RegistryConfiguration,
        dryRun: Boolean
    ): String {
        return if (dryRun && !configuration.useGit) {
            "file://${project.rootProject.buildDir.absolutePath}/${UUID.randomUUID()}"
        } else {
            configuration.url
        }
    }

    private fun setRepository(
        project: Project,
        repository: MavenArtifactRepository,
        configuration: PublishingApiContract.RegistryConfiguration,
        dryRun: Boolean
    ) {
        repository.name = configuration.name
        repository.setUrl(
            getUrl(
                project,
                configuration,
                dryRun
            )
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
        configuration: List<PublishingApiContract.RegistryConfiguration>,
        dryRun: Boolean
    ) {
        project.extensions.configure(PublishingExtension::class.java) {
            repositories {
                setRepositories(
                    project,
                    this,
                    configuration,
                    dryRun
                )
            }
        }
    }
}
