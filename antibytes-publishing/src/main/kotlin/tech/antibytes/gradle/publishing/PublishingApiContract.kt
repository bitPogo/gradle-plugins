/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import com.palantir.gradle.gitversion.VersionDetails

interface PublishingApiContract {
    data class VersionInfo(
        val name: String,
        val details: VersionDetails
    )

    interface PomConfiguration {
        val name: String
        val description: String
        val year: Int
        val url: String
        val additionalInformation: Map<String, String>
    }

    interface ContributorConfiguration {
        val name: String
        val email: String
        val url: String?
        val additionalInformation: Map<String, String>
    }

    interface DeveloperConfiguration : ContributorConfiguration {
        val id: String
    }

    interface LicenseConfiguration {
        val name: String
        val url: String
        val distribution: String
    }

    interface SourceControlConfiguration {
        val connection: String
        val url: String
        val developerConnection: String
    }

    interface PackageConfiguration {
        val artifactId: String?
        val groupId: String?
        val version: String?
        val pom: PomConfiguration
        val developers: List<DeveloperConfiguration>
        val contributors: List<ContributorConfiguration>
        val license: LicenseConfiguration
        val scm: SourceControlConfiguration
    }

    interface RegistryConfiguration {
        val useGitFlow: Boolean
        val gitWorkDirectory: String
        val packageRegistryUri: String
        val packageRegistryUsername: String
        val packageRegistryPassword: String
    }

    data class PublishingConfiguration(
        val registryConfiguration: List<RegistryConfiguration>,
        val packageConfiguration: PackageConfiguration,
        val dryRun: Boolean = false,
    )
}
