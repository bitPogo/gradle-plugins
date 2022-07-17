/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract

internal interface PublisherContract {
    interface GitRepository {
        fun configureCloneTask(
            project: Project,
            configuration: PublishingApiContract.RepositoryConfiguration,
        ): Task?

        fun configurePushTask(
            project: Project,
            configuration: PublishingApiContract.RepositoryConfiguration,
            version: String,
            dryRun: Boolean,
        ): Task?
    }

    fun interface MavenPublisher {
        fun configure(
            project: Project,
            configuration: PublishingApiContract.PackageConfiguration,
            docs: Task?,
            version: String,
        )
    }

    fun interface MavenRepository {
        fun configure(
            project: Project,
            configuration: PublishingApiContract.RepositoryConfiguration,
            dryRun: Boolean,
        )
    }
}
