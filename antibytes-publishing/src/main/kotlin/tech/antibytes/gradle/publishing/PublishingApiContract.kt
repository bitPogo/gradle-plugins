/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache 2.0
 */

package tech.antibytes.gradle.publishing

import com.palantir.gradle.gitversion.VersionDetails

interface PublishingApiContract {
    interface VersioningConfiguration {
        val releasePattern: Regex
        val featurePattern: Regex
        val dependencyBotPattern: Regex
        val issuePattern: Regex?

        val versionPrefix: String
        val normalization: Set<String>
    }

    data class VersionInfo(
        val name: String,
        val details: VersionDetails
    )

    data class VersioningConfigurationContainer(
        override val releasePattern: Regex = "main|release/.*".toRegex(),
        override val featurePattern: Regex = "feature/(.*)".toRegex(),
        override val dependencyBotPattern: Regex = "dependabot/(.*)".toRegex(),
        override val issuePattern: Regex? = null,
        override val versionPrefix: String = "v",
        override val normalization: Set<String> = emptySet()
    ) : VersioningConfiguration

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
        val pom: PomConfiguration
        val developers: List<DeveloperConfiguration>
        val contributors: List<ContributorConfiguration>
        val license: LicenseConfiguration
        val scm: SourceControlConfiguration
    }

    interface Credentials {
        val username: String?
        val password: String?
    }

    interface RepositoryConfiguration : Credentials {
        val name: String
        val url: String
    }

    interface RegistryConfiguration : RepositoryConfiguration {
        val useGit: Boolean
        val gitWorkDirectory: String
    }
}
