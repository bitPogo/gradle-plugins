/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import java.io.File

interface PublishingApiContract {
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

    interface DocumentationConfiguration {
        val tasks: Set<String>
        val outputDir: File
    }

    interface PackageConfiguration {
        val artifactId: String?
        val groupId: String?
        val isPureJavaLibrary: Boolean
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

    interface MemorySigning {
        val key: String?
        val password: String?
    }

    interface CompleteMemorySigning : MemorySigning {
        val keyId: String?
    }
}
