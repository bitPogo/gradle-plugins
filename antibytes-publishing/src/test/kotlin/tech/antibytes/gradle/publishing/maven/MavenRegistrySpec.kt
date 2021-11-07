/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.maven

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.PublishingExtension
import org.junit.Test
import tech.antibytes.gradle.publishing.invokeGradleAction
import tech.antibytes.gradle.publishing.publicApi.RegistryConfiguration
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MavenRegistrySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils MavenRegistry`() {
        val registry: Any = MavenRegistry

        assertTrue(registry is MavenContract.MavenRegistry)
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it setups up the RepositoryConfiguration`() {
        // Given
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = false,
            gitWorkDirectory = "",
            url = fixture(),
            username = fixture(),
            password = fixture()
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val credentials: PasswordCredentials = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { publishingExtension.repositories } returns repositoryContainer
        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk()
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk()
        )

        // When
        MavenRegistry.configure(
            project,
            configuration,
            false
        )

        // Then
        verify(exactly = 1) { repository.name = configuration.name.capitalize() }
        verify(exactly = 1) { repository.setUrl(configuration.url) }

        verify(exactly = 1) { credentials.username = configuration.username }
        verify(exactly = 1) { credentials.password = configuration.password }
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it will not set credentials if UseGitHub is true`() {
        // Given
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = true,
            gitWorkDirectory = "",
            url = fixture(),
            username = fixture(),
            password = fixture()
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val credentials: PasswordCredentials = mockk(relaxed = true)

        every { project.rootProject.buildDir } returns File(fixture<String>())
        every { project.extensions } returns extensions
        every { publishingExtension.repositories } returns repositoryContainer
        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk()
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk()
        )

        // When
        MavenRegistry.configure(
            project,
            configuration,
            false
        )

        // Then
        verify(exactly = 0) { credentials.username = any() }
        verify(exactly = 0) { credentials.password = any() }
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it will not set credentials if DryRun is true`() {
        // Given
        val dryRun = true
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = false,
            gitWorkDirectory = "",
            url = fixture(),
            username = fixture(),
            password = fixture()
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val credentials: PasswordCredentials = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns mockk(relaxed = true)
        every { publishingExtension.repositories } returns repositoryContainer
        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk()
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk()
        )

        // When
        MavenRegistry.configure(
            project,
            configuration,
            dryRun
        )

        // Then
        verify(exactly = 0) { credentials.username = any() }
        verify(exactly = 0) { credentials.password = any() }
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it replaces the given URL with a location in the RootProjects build directory if DryRun is true and UseGit is false`() {
        // Given
        val dryRun = true
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = false,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture()
        )
        val rootBuildDir = "somewhere"

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val credentials: PasswordCredentials = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk()
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk()
        )

        // When
        MavenRegistry.configure(
            project,
            configuration,
            dryRun
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = "file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}"
        )
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it replaces the given URL with a location in the RootProjects build directory if DryRun is false and UseGit is true`() {
        // Given
        val dryRun = false
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = true,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture()
        )
        val rootBuildDir = "somewhere"

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val credentials: PasswordCredentials = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk()
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk()
        )

        // When
        MavenRegistry.configure(
            project,
            configuration,
            dryRun
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = "file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}"
        )
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it replaces the given URL with a location in the RootProjects build directory if DryRun is true and UseGit is true`() {
        // Given
        val dryRun = true
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = true,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture()
        )
        val rootBuildDir = "somewhere"

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val credentials: PasswordCredentials = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk()
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk()
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk()
        )

        // When
        MavenRegistry.configure(
            project,
            configuration,
            dryRun
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = "file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}"
        )
    }
}
