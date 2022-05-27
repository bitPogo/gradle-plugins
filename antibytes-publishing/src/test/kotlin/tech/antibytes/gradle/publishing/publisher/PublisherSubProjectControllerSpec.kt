/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRepository
import tech.antibytes.gradle.verisoning.VersioningContract.VersioningConfiguration
import kotlin.test.assertTrue

class PublisherSubProjectControllerSpec {
    private val fixture = kotlinFixture()
    private val gitRegistryTestConfig = GitRepositoryConfiguration(
        username = "",
        password = "",
        name = "",
        url = "",
        gitWorkDirectory = ""
    )
    private val mavenRegistryTestConfig = MavenRepositoryConfiguration(
        username = "",
        password = "",
        name = "",
        url = "",
    )

    @BeforeEach
    fun setUp() {
        mockkObject(MavenPublisher)
        mockkObject(MavenRepository)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(MavenPublisher)
        unmockkObject(MavenRepository)
    }

    @Test
    fun `It fulfils PublisherController`() {
        val controller: Any = PublisherSubProjectController

        assertTrue(controller is PublishingContract.PublisherController)
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no PackageConfiguration was given`() {
        // Given
        val project: Project = mockk()
        val config = TestConfig(
            repositoryConfiguration = setOf(mockk()),
            packageConfiguration = null,
            dryRun = false,
            excludeProjects = setOf(),
            versioning = mockk(),
            standalone = true
        )

        every { project.name } returns fixture()

        // When
        PublisherSubProjectController.configure(
            project,
            "version",
            config,
        )

        // Then
        verify(exactly = 0) { MavenPublisher.configure(project, any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no registryConfiguration was given`() {
        // Given
        val project: Project = mockk()
        val config = TestConfig(
            repositoryConfiguration = emptySet(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = emptySet(),
            versioning = mockk(),
            standalone = true,
        )

        every { project.name } returns fixture()
        every { project.tasks } returns mockk()

        // When
        PublisherSubProjectController.configure(
            project,
            "version",
            config,
        )

        // Then
        verify(exactly = 0) { MavenPublisher.configure(project, any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it distributes the configurations`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = false
        )

        every { MavenPublisher.configure(project, packageConfiguration, version) } just Runs
        every { MavenRepository.configure(project, or(registry1, registry2), dryRun) } just Runs

        // When
        PublisherSubProjectController.configure(
            project,
            version,
            config,
        )

        // Then
        verify(exactly = 1) { MavenPublisher.configure(project, packageConfiguration, version) }

        verify(exactly = 1) {
            MavenRepository.configure(
                project,
                registry1,
                dryRun
            )
        }

        verify(exactly = 1) {
            MavenRepository.configure(
                project,
                registry2,
                dryRun
            )
        }
    }
}
