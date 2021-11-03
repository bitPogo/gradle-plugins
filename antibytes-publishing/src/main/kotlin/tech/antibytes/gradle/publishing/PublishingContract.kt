/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import org.gradle.api.Project
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

    fun interface Configurator {
        fun configure(
            project: Project,
            configuration: PublishingApiContract.PublishingConfiguration
        )
    }

    fun interface MavenPublisher : Configurator

    fun interface MavenRepository : Configurator

    fun interface GithubRepository : Configurator

    fun interface PublisherController : Configurator

    interface PublishingPluginConfiguration : VersioningConfiguration {
        val publishingConfigurations: Property<PublishingApiContract.PublishingConfiguration?>
    }
}
