/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import tech.antibytes.gradle.publishing.publicApi.VersionInfo

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

    interface PublishingPluginConfiguration {
        val excludeProjects: SetProperty<String>
        val versioning: Property<PublishingApiContract.VersioningConfiguration>
        val registryConfiguration: SetProperty<PublishingApiContract.RegistryConfiguration>
        val packageConfiguration: Property<PublishingApiContract.PackageConfiguration>
        val dryRun: Property<Boolean>
    }

    fun interface PublisherController {
        fun configure(
            project: Project,
            configuration: PublishingPluginConfiguration
        )
    }
}
