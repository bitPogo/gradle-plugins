/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.dependency.catalog.addSharedAntibytesConfiguration
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration
import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.Type
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration

plugins {
    `version-catalog`

    id("tech.antibytes.gradle.publishing.local")
}

// To make it available as direct dependency
group = LibraryConfig.group

antibytesPublishing {
    versioning.set(
        VersioningConfiguration(
            featurePrefixes = listOf("feature"),
            suppressSnapshot = true
        )
    )
    packaging.set(
        PackageConfiguration(
            groupId = LibraryConfig.group,
            type = Type.VERSION_CATALOG,
            pom = PomConfiguration(
                name = name,
                description = "General dependencies for Antibytes projects.",
                year = 2022,
                url = LibraryConfig.publishing.url,
            ),
            developers = listOf(
                DeveloperConfiguration(
                    id = LibraryConfig.publishing.developerId,
                    name = LibraryConfig.publishing.developerName,
                    url = LibraryConfig.publishing.developerUrl,
                    email = LibraryConfig.publishing.developerEmail,
                )
            ),
            license = LicenseConfiguration(
                name = LibraryConfig.publishing.licenseName,
                url = LibraryConfig.publishing.licenseUrl,
                distribution = LibraryConfig.publishing.licenseDistribution,
            ),
            scm = SourceControlConfiguration(
                url = LibraryConfig.publishing.scmUrl,
                connection = LibraryConfig.publishing.scmConnection,
                developerConnection = LibraryConfig.publishing.scmDeveloperConnection,
            ),
        )
    )
    repositories.set(
        setOf(
            GitRepositoryConfiguration(
                name = "Development",
                gitWorkDirectory = "dev",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-dev",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            ),
            GitRepositoryConfiguration(
                name = "Snapshot",
                gitWorkDirectory = "snapshots",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-snapshots",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            ),
            GitRepositoryConfiguration(
                name = "RollingRelease",
                gitWorkDirectory = "rolling",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-rolling-releases",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            ),
            GitRepositoryConfiguration(
                name = "Release",
                gitWorkDirectory = "releases",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-releases",
                username = LibraryConfig.username,
                password = LibraryConfig.password
            ),
            MavenRepositoryConfiguration(
                name = "Local",
                url = uri(rootProject.layout.buildDirectory),
            ),
        )
    )
}

catalog {
    addSharedAntibytesConfiguration()
}

val addSharedConfiguration by tasks.creating(Copy::class.java) {
    dependsOn("generateCatalogAsToml")
    mustRunAfter("clean")

    include("libs.versions.toml")
    from(File(layout.buildDirectory.asFile.get(), "version-catalog/libs.versions.toml"))
    into(File(rootProject.projectDir, "gradle"))

    filter { content ->
        content.replace(
            "^gradle-antibytes([\\-a-zA-Z]+)\\s*=\\s*\"[\\-0-9a-zA-Z]+\"".toRegex(),
            "gradle-antibytes$1 = \"xxx\""
        )
    }
    rename {
        "antibytes.catalog.toml"
    }
}

tasks.named("check") {
    dependsOn(addSharedConfiguration)
}
