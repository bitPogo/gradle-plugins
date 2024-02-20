/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.component.CustomComponentApiContract.Artifact
import tech.antibytes.gradle.versioning.api.VersioningConfiguration
import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration
import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.Type
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration

plugins {
    id("tech.antibytes.gradle.component.local")
    id("tech.antibytes.gradle.publishing.local")
}

val artifactName = "shared-detekt-configuration"
val componentAttributes = mutableMapOf<Any, Any>(
    Category.CATEGORY_ATTRIBUTE to objects.named<Category>(Category.LIBRARY),
    Usage.USAGE_ATTRIBUTE to objects.named<Usage>("shared-detekt-configuration"),
    Bundling.BUNDLING_ATTRIBUTE to objects.named<Bundling>(Bundling.EXTERNAL),
    LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE to objects.named<LibraryElements>(artifactName)
)
val artifacts = listOf(
    Artifact(
        name = artifactName,
        type = "yml",
        typeExtension = "yml",
        componentHandle = file("${projectDir.absolutePath.trimEnd('/')}/src/config.yml"),
        classifier = "",
    )
)

// To make it available as direct dependency
group = LibraryConfig.group

antibytesCustomComponent {
    customArtifacts.set(artifacts)
    attributes.set(componentAttributes)
}

antibytesPublishing {
    versioning.set(
        VersioningConfiguration(
            featurePrefixes = listOf("feature"),
            suppressSnapshot = true,
        )
    )
    packaging.set(
        PackageConfiguration(
            custom = "antibytesCustomComponent",
            groupId = LibraryConfig.group,
            type = Type.CUSTOM_COMPONENT,
            pom = PomConfiguration(
                name = name,
                description = "General configuration for Detekt for Antibytes projects.",
                year = 2022,
                url = LibraryConfig.publishing.url,
                packageing = "yml",
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
