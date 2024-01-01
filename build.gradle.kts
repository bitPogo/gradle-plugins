/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

import tech.antibytes.gradle.dependency.helper.GradleCompositeBuilds
import tech.antibytes.gradle.dependency.helper.ensureKotlinVersion
import tech.antibytes.gradle.plugin.config.LibraryConfig
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

plugins {
    id("tech.antibytes.gradle.dependency.catalog")
    id("tech.antibytes.gradle.dependency.helper.local")
    id("tech.antibytes.gradle.publishing.local")
    id("tech.antibytes.gradle.quality.local")
}

antibytesPublishing {
    versioning.set(
        VersioningConfiguration(
            featurePrefixes = emptyList(),
            suppressSnapshot = true,
        ),
    )
    repositories.set(
        setOf(
            GitRepositoryConfiguration(
                name = "Development",
                gitWorkDirectory = "dev",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-dev",
                username = LibraryConfig.username,
                password = LibraryConfig.password,
            ),
            GitRepositoryConfiguration(
                name = "Snapshot",
                gitWorkDirectory = "snapshots",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-snapshots",
                username = LibraryConfig.username,
                password = LibraryConfig.password,
            ),
            GitRepositoryConfiguration(
                name = "RollingRelease",
                gitWorkDirectory = "rolling",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-rolling-releases",
                username = LibraryConfig.username,
                password = LibraryConfig.password,
            ),
            GitRepositoryConfiguration(
                name = "Release",
                gitWorkDirectory = "releases",
                url = "https://github.com/${LibraryConfig.githubOwner}/maven-releases",
                username = LibraryConfig.username,
                password = LibraryConfig.password,
            ),
            MavenRepositoryConfiguration(
                name = "Local",
                url = uri(rootProject.layout.buildDirectory),
            ),
        ),
    )
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }

    ensureKotlinVersion(libs.versions.kotlin.get())
}

GradleCompositeBuilds.configure(project)

tasks.named<Wrapper>("wrapper") {
    gradleVersion = libs.versions.gradle.get()
    distributionType = Wrapper.DistributionType.ALL
}
