/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.git

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingError
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.util.capitalize

internal object GitRepository : PublisherContract.GitRepository {
    override fun configureCloneTask(
        project: Project,
        configuration: RepositoryConfiguration<out Any>,
    ): Task? {
        return if (configuration is GitRepositoryConfiguration) {
            project.tasks.create("clone${configuration.name.capitalize()}") {
                doLast {
                    GitActions.checkout(
                        project,
                        configuration,
                    )
                }
            }
        } else {
            null
        }
    }

    override fun configurePushTask(
        project: Project,
        configuration: RepositoryConfiguration<out Any>,
        version: String,
        dryRun: Boolean,
        publishingId: String,
    ): Task? {
        return if (configuration is GitRepositoryConfiguration) {
            project.tasks.create("push${configuration.name.capitalize()}${publishingId.capitalize()}") {
                doLast {
                    val succeeded = GitActions.push(
                        project,
                        configuration,
                        version,
                        dryRun,
                    )

                    if (!succeeded) {
                        throw PublishingError.GitRejectedCommitError(
                            "Something went wrong while pushing, please manually check the repository.",
                        )
                    }
                }
            }
        } else {
            null
        }
    }
}
