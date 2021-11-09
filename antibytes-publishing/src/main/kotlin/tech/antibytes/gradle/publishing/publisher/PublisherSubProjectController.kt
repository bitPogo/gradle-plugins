/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.Versioning
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRegistry

internal object PublisherSubProjectController : PublishingContract.PublisherController {
    private fun isApplicable(
        configuration: PublishingContract.PublishingPluginConfiguration
    ): Boolean {
        return configuration.registryConfiguration.isNotEmpty() &&
            configuration.packageConfiguration is PublishingApiContract.PackageConfiguration
    }

    override fun configure(
        project: Project,
        configuration: PublishingContract.PublishingPluginConfiguration
    ) {
        if (isApplicable(configuration)) {
            val version = Versioning.versionName(
                project,
                configuration.versioning
            )

            MavenPublisher.configure(
                project,
                configuration.packageConfiguration as PublishingApiContract.PackageConfiguration,
                version
            )

            configuration.registryConfiguration.forEach { registry ->
                MavenRegistry.configure(
                    project,
                    registry,
                    configuration.dryRun
                )
            }
        }
    }
}
