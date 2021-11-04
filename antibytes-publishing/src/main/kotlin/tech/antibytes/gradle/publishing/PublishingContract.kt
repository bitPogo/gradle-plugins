/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

internal interface PublishingContract {
    interface VersioningConfiguration {
        val releasePattern: Property<Regex>
        val featurePattern: Property<Regex>
        val dependencyBotPattern: Property<Regex>
        val issuePattern: Property<Regex?>

        val versionPrefix: Property<String>
        val normalization: SetProperty<String>
    }

    interface Versioning {
        fun versionName(
            project: Project,
            configuration: VersioningConfiguration
        ): String

        fun versionInfo(
            project: Project,
            configuration: VersioningConfiguration
        ): PublishingApiContract.VersionInfo

        companion object {
            const val SEPARATOR = "-"
            const val NON_RELEASE_SUFFIX = "SNAPSHOT"
        }
    }

    interface PublishingConfiguration {
        val registryConfiguration: ListProperty<PublishingApiContract.RegistryConfiguration>
        val packageConfiguration: Property<PublishingApiContract.PackageConfiguration>
        val dryRun: Property<Boolean>
    }

    fun interface PublisherController {
        fun configure(
            project: Project,
            configuration: PublishingConfiguration
        )
    }

    interface PublishingPluginConfiguration : VersioningConfiguration, PublishingConfiguration {
        val excludeProjects: SetProperty<String>
    }
}
