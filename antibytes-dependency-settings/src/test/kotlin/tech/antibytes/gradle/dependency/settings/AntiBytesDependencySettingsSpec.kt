/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.RepositoryContentDescriptor
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.api.initialization.resolve.DependencyResolutionManagement
import org.gradle.api.initialization.resolve.MutableVersionCatalogContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.dependency.settings.config.MainConfig
import tech.antibytes.gradle.test.invokeGradleAction

@Suppress("UnstableApiUsage")
class AntiBytesDependencySettingsSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesDependencySettings()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called it add additional repositories and VersionCatalog`() {
        // Given
        val settings: Settings = mockk(relaxed = true)
        val dependencyResolutionManagement: DependencyResolutionManagement = mockk(relaxed = true)
        val repositoryHandler: RepositoryHandler = mockk(relaxed = true)
        val mavenArtifactRepository: MavenArtifactRepository = mockk(relaxed = true)
        val contentDescriptor: RepositoryContentDescriptor = mockk(relaxed = true)
        val versionCatalog: MutableVersionCatalogContainer = mockk(relaxed = true)
        val versionCatalogBuilder: VersionCatalogBuilder = mockk(relaxed = true)

        every { settings.dependencyResolutionManagement } returns dependencyResolutionManagement
        every { dependencyResolutionManagement.repositories } returns repositoryHandler
        every { dependencyResolutionManagement.versionCatalogs } returns versionCatalog
        every { dependencyResolutionManagement.versionCatalogs } returns versionCatalog
        every { versionCatalog.create(any()) } returns versionCatalogBuilder
        invokeGradleAction(
            { probe -> repositoryHandler.maven(probe) },
            mavenArtifactRepository,
            mavenArtifactRepository,
        )
        invokeGradleAction(
            { probe -> mavenArtifactRepository.content(probe) },
            contentDescriptor,
        )

        // When
        AntiBytesDependencySettings().apply(settings)

        // Then
        verify(exactly = 1) {
            mavenArtifactRepository.setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
        }
        verify(exactly = 1) {
            mavenArtifactRepository.setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
        }
        verify(exactly = 1) {
            mavenArtifactRepository.setUrl(MainConfig.gradlePluginsDir)
        }
        verify(exactly = 3) {
            contentDescriptor.includeGroupByRegex(MainConfig.pluginGroup)
        }
        verify(exactly = 1) {
            versionCatalog.create("antibytesCatalog")
        }
        verify(exactly = 1) {
            versionCatalogBuilder.from(
                "tech.antibytes.gradle:antibytes-dependency-catalog:${MainConfig.antibytesVersion}",
            )
        }
    }
}
