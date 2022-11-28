/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import tech.antibytes.gradle.publishing.PublishingApiContract.DocumentationConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.MemorySigning
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

internal interface PublishingContract {
    interface PublishingPluginExtension {
        val excludeProjects: SetProperty<String>
        val versioning: Property<VersioningConfiguration>
        val repositories: SetProperty<RepositoryConfiguration>
        val packaging: Property<PackageConfiguration>
        val signing: Property<MemorySigning>
        val documentation: Property<DocumentationConfiguration>
        val dryRun: Property<Boolean>
        val standalone: Property<Boolean>
    }

    interface PublisherController {
        fun configure(
            project: Project,
            version: String = "",
            documentation: Task? = null,
            extension: PublishingPluginExtension,
        )
    }

    interface SigningController {
        fun configure(
            project: Project,
            extension: PublishingPluginExtension,
        )
    }

    companion object {
        const val EXTENSION_ID = "antiBytesPublishing"
        val DEPENDENCIES = arrayOf(
            "com.palantir.git-version",
            "maven-publish",
            "org.gradle.signing",
        )
    }
}
