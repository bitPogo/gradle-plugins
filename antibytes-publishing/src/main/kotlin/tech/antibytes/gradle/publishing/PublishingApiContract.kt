/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

interface PublishingApiContract {
    interface VersioningConfiguration {
        val releasePrefixes: List<String>
        val featurePrefixes: List<String>
        val dependencyBotPrefixes: List<String>
        val issuePattern: Regex?
        val useGitHashFeatureSuffix: Boolean

        val versionPrefix: String
        val normalization: Set<String>
    }

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
        val username: String
        val password: String
    }

    interface RepositoryConfiguration : Credentials {
        val name: String
        val url: String
    }

    interface GitRepositoryConfiguration : RepositoryConfiguration {
        val gitWorkDirectory: String
    }

    interface MavenRepositoryConfiguration : RepositoryConfiguration
}
