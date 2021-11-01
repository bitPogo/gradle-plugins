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

    interface GeneralPackageInformation {
        val artifactId: String
        val name: String
        val groupId: String
        val description: String
        val year: Int
        val url: String
        val additionalInformation: Map<String, String>
    }

    interface DeveloperInformation {
        val id: String
        val name: String
        val email: String
        val url: String?
        val additionalInformation: Map<String, String>
    }

    interface ContributorInformation {
        val name: String
        val email: String
        val url: String?
        val additionalInformation: Map<String, String>
    }

    interface LicenseInformation {
        val name: String
        val url: String
        val distribution: String
    }

    interface SourceControl {
        val connection: String
        val url: String
        val developerConnection: String
    }

    interface PackageConfiguration {
        val general: GeneralPackageInformation
        val developers: List<DeveloperInformation>
        val contributors: List<ContributorInformation>
        val license: LicenseInformation
        val scm: SourceControl
    }

    data class PackageRegistry(
        val useGitFlow: Boolean,
        val gitWorkingDirectory: String,
        val packageRegistryURI: String,
        val packageRegistryUsername: String,
        val packageRegistryPassword: String,
        val packageConfiguration: PackageConfiguration,
    )
}
