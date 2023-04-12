/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension

internal typealias Version = String

internal interface PublisherContract {
    interface GitRepository {
        fun configureCloneTask(
            project: Project,
            configuration: RepositoryConfiguration<out Any>,
        ): Task?

        fun configurePushTask(
            project: Project,
            configuration: RepositoryConfiguration<out Any>,
            version: String,
            dryRun: Boolean,
            publishingId: String = "",
        ): Task?
    }

    fun interface MavenPublisher {
        fun configure(
            project: Project,
            configuration: PackageConfiguration,
            docs: Task?,
            version: String,
        )
    }

    fun interface MavenRepository {
        fun configure(
            project: Project,
            configuration: RepositoryConfiguration<out Any>,
            dryRun: Boolean,
        )
    }

    fun interface VersionController {
        fun configure(
            project: Project,
            configuration: PublishingPluginExtension,
        ): Version
    }

    fun interface DocumentationController {
        fun configure(
            project: Project,
            configuration: PublishingPluginExtension,
        ): Task?
    }
}
