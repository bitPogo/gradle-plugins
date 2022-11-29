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
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRepository
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeSetProperty
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

class PublisherSubProjectControllerSpec {
    private val fixture = kotlinFixture()
    private val gitRegistryTestConfig = GitRepositoryConfiguration(
        username = "",
        password = "",
        name = "",
        url = "",
        gitWorkDirectory = "",
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
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(PackageConfiguration::class.java, null),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, false),
        )
        val documentation: Task = mockk()

        every { project.name } returns fixture()

        // When
        PublisherSubProjectController.configure(
            project,
            "version",
            documentation,
            config,
        )

        // Then
        verify(exactly = 0) { MavenPublisher.configure(project, any(), any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no Repositories were given`() {
        // Given
        val project: Project = mockk()
        val rootProject: Project = mockk()

        val config = TestConfig(
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                emptySet(),
            ),
            packaging = makeProperty(PackageConfiguration::class.java, mockk()),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, false),
        )
        val rootConfig = TestConfig(
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                emptySet(),
            ),
            packaging = makeProperty(PackageConfiguration::class.java, mockk()),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, false),
        )
        val documentation: Task = mockk()

        every { project.rootProject } returns rootProject
        every {
            rootProject.extensions.getByType(PublishingPluginExtension::class.java)
        } returns rootConfig
        every { project.name } returns fixture()
        every { project.tasks } returns mockk()

        // When
        PublisherSubProjectController.configure(
            project,
            "version",
            documentation,
            config,
        )

        // Then
        verify(exactly = 0) { MavenPublisher.configure(project, any(), any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it distributes the configurations`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()
        val documentation: Task = mockk()

        val repositoriesConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositories = makeSetProperty(RepositoryConfiguration::class.java, repositoriesConfiguration),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(VersioningConfiguration::class.java, versioningConfiguration),
            standalone = makeProperty(Boolean::class.java, false),
        )

        every { MavenPublisher.configure(project, packageConfiguration, any(), version) } just Runs
        every { MavenRepository.configure(project, or(registry1, registry2), dryRun) } just Runs

        // When
        PublisherSubProjectController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verify(exactly = 1) { MavenPublisher.configure(project, packageConfiguration, documentation, version) }

        verify(exactly = 1) {
            MavenRepository.configure(
                project,
                registry1,
                dryRun,
            )
        }

        verify(exactly = 1) {
            MavenRepository.configure(
                project,
                registry2,
                dryRun,
            )
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it distributes the configurations, while fetching the repository from its root`() {
        // Given
        val project: Project = mockk()
        val rootProject: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()
        val documentation: Task = mockk()

        val repositoriesConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositories = makeSetProperty(RepositoryConfiguration::class.java, emptySet()),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(VersioningConfiguration::class.java, versioningConfiguration),
            standalone = makeProperty(Boolean::class.java, false),
        )

        val rootConfig = TestConfig(
            repositories = makeSetProperty(RepositoryConfiguration::class.java, repositoriesConfiguration),
            packaging = makeProperty(PackageConfiguration::class.java, null),
            dryRun = makeProperty(Boolean::class.java, null),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(VersioningConfiguration::class.java, versioningConfiguration),
            standalone = makeProperty(Boolean::class.java, false),
        )

        every { project.rootProject } returns rootProject
        every {
            rootProject.extensions.getByType(PublishingPluginExtension::class.java)
        } returns rootConfig
        every { MavenPublisher.configure(project, packageConfiguration, any(), version) } just Runs
        every { MavenRepository.configure(project, or(registry1, registry2), dryRun) } just Runs

        // When
        PublisherSubProjectController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verify(exactly = 1) { MavenPublisher.configure(project, packageConfiguration, documentation, version) }
    }
}
