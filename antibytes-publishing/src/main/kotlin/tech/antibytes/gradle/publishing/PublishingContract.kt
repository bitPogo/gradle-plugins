/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.api.VersionInfo

internal interface PublishingContract {
    interface Versioning {
        fun versionName(
            project: Project,
            configuration: PublishingApiContract.VersioningConfiguration
        ): String

        fun versionInfo(
            project: Project,
            configuration: PublishingApiContract.VersioningConfiguration
        ): VersionInfo

        companion object {
            const val SEPARATOR = "-"
            const val NON_RELEASE_SUFFIX = "SNAPSHOT"
        }
    }

    interface PublishingPluginExtension {
        var excludeProjects: Set<String>
        var versioning: PublishingApiContract.VersioningConfiguration
        var repositoryConfiguration: Set<PublishingApiContract.RepositoryConfiguration>
        var packageConfiguration: PublishingApiContract.PackageConfiguration?
        var signingConfiguration: SigningApiContract.MemorySigning?
        var dryRun: Boolean
        var standalone: Boolean
    }

    interface PublisherController {
        fun configure(
            project: Project,
            version: String = "",
            extension: PublishingPluginExtension
        )
    }

    companion object {
        const val EXTENSION_ID = "antiBytesPublishing"
        val DEPENDENCIES = arrayOf(
            "com.palantir.git-version",
            "maven-publish",
            "org.gradle.signing"
        )
    }
}
