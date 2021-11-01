/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import com.palantir.gradle.gitversion.VersionDetails
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

data class VersionInfo(
    val name: String,
    val details: VersionDetails
)

internal interface PublishingContract {
    interface VersioningConfiguration {
        val releasePattern: Property<Regex>
        val featurePattern: Property<Regex>
        val dependencyBotPattern: Property<Regex>
        val issuePattern: Property<Regex?>

        val versionPrefix: Property<String>
        val normalization: ListProperty<String>
    }

    interface Versioning {
        fun versionName(): String
        fun versionInfo(): VersionInfo

        companion object {
            const val SEPARATOR = "-"
            const val NON_RELEASE_SUFFIX = "SNAPSHOT"
        }
    }

    interface PublishingConfiguration : VersioningConfiguration
}
