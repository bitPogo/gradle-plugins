/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import com.palantir.gradle.gitversion.VersionDetails
import org.gradle.api.provider.Property

data class VersionInfo(
    val name: String,
    val details: VersionDetails
)

internal interface PublishingContract {
    interface PublishingConfiguration {
        val releasePattern: Property<Regex>
        val featurePattern: Property<Regex>
        val issuePattern: Property<Regex?>
    }

    interface Versioning {
        fun versionName(): String
        fun versionInfo(): VersionInfo
    }
}
