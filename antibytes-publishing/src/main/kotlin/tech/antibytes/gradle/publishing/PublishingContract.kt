/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

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
        fun versionName(): String
        fun versionInfo(): PublishingApiContract.VersionInfo

        companion object {
            const val SEPARATOR = "-"
            const val NON_RELEASE_SUFFIX = "SNAPSHOT"
        }
    }

    fun interface MavenPublishing {
        fun configureMavenTask(configuration: PublishingApiContract.PackageRegistry)
    }

    interface PublishingConfiguration : VersioningConfiguration {
        val dryRun: Property<Boolean>
        val packageRegistries: SetProperty<PublishingApiContract.PackageRegistry>
    }
}
