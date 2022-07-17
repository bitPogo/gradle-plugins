/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.versioning.VersioningContract

internal interface PublishingContract {
    interface PublishingPluginExtension {
        var excludeProjects: Set<String>
        var versioning: VersioningContract.VersioningConfiguration
        var repositoryConfiguration: Set<PublishingApiContract.RepositoryConfiguration>
        var packageConfiguration: PublishingApiContract.PackageConfiguration?
        var signingConfiguration: PublishingApiContract.MemorySigning?
        var documentation: PublishingApiContract.DocumentationConfiguration?
        var dryRun: Boolean
        var standalone: Boolean
    }

    interface PublisherController {
        fun configure(
            project: Project,
            version: String = "",
            documentation: Task? = null,
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
