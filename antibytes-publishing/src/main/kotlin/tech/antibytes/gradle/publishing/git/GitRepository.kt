/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingError

internal object GitRepository : GitContract.GitRepository {
    private fun configureTasks(
        project: Project,
        configuration: PublishingApiContract.RegistryConfiguration,
        version: String,
        dryRun: Boolean
    ) {
        project.tasks.create("clone${configuration.name.capitalize()}") {
            doLast {
                GitActions.checkout(
                    project,
                    configuration
                )
            }
        }

        project.tasks.create("push${configuration.name.capitalize()}") {
            dependsOn(
                "publishAllPublicationsTo${configuration.name.capitalize()}PackagesRepository"
            )

            doLast {
                val succeeded = GitActions.push(
                    project,
                    configuration,
                    version,
                    dryRun
                )

                if (!succeeded) {
                    throw PublishingError.GitRejectedCommitError(
                        "Something went wrong while pushing, please manually check the repository."
                    )
                }
            }
        }
    }

    override fun configure(
        project: Project,
        configurations: List<PublishingApiContract.RegistryConfiguration>,
        version: String,
        dryRun: Boolean
    ) {
        configurations.forEach { configuration ->
            if (configuration.useGit) {
                configureTasks(
                    project,
                    configuration,
                    version,
                    dryRun
                )
            }
        }
    }
}
