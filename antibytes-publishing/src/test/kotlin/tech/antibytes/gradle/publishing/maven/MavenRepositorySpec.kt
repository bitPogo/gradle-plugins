/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
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
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.PublishingExtension
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.test.invokeGradleAction

class MavenRepositorySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils MavenRegistry`() {
        val registry: Any = MavenRepository

        assertTrue(registry is PublisherContract.MavenRepository)
    }

    @Test
    fun `Given configure is called with a Project, MavenRepositoryConfiguration and a DryRun flag, it setups up the RepositoryConfiguration`() {
        // Given
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
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
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            false,
        )

        // Then
        verify(exactly = 1) { repository.name = configuration.name.capitalize() }
        verify(exactly = 1) { repository.setUrl(configuration.url) }

        verify(exactly = 1) { credentials.username = configuration.username }
        verify(exactly = 1) { credentials.password = configuration.password }
    }

    @Test
    fun `Given configure is called with a Project, GitRepositoryConfiguration and a DryRun flag, it will not set credentials`() {
        // Given
        val configuration = GitRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
            gitWorkDirectory = fixture(),
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
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            false,
        )

        // Then
        verify(exactly = 0) { credentials.username = any() }
        verify(exactly = 0) { credentials.password = any() }
    }

    @Test
    fun `Given configure is called with a Project, MavenRepositoryConfiguration and a DryRun flag, it will not set credentials if DryRun is true`() {
        // Given
        val dryRun = true
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
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
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            credentials,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            dryRun,
        )

        // Then
        verify(exactly = 0) { credentials.username = any() }
        verify(exactly = 0) { credentials.password = any() }
    }

    @Test
    fun `Given configure is called with a Project, MavenRepositoryConfiguration and a DryRun flag, it resolves the given URI`() {
        // Given
        val dryRun = false
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
        )

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            dryRun,
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = configuration.url,
        )
    }

    @Test
    fun `Given configure is called with a Project, MavenRepositoryConfiguration and a DryRun flag, it resolves a local URI if DryRun is true`() {
        // Given
        val dryRun = true
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
        )
        val rootBuildDir = "somewhere"

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            dryRun,
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = "file://${File(rootBuildDir).absolutePath}/${configuration.name}/dryRun",
        )
    }

    @Test
    fun `Given configure is called with a Project, GitRepositoryConfiguration and a DryRun flag, it resolves a local URI`() {
        // Given
        val dryRun = false
        val configuration = GitRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
            gitWorkDirectory = fixture(),
        )
        val rootBuildDir = "somewhere"

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            dryRun,
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = "file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}",
        )
    }

    @Test
    fun `Given configure is called with a Project, GitRepositoryConfiguration and a DryRun flag, it resolves a local URI and uses the WorkingDirectory over DryRun`() {
        // Given
        val dryRun = true
        val configuration = GitRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
            gitWorkDirectory = fixture(),
        )
        val rootBuildDir = "somewhere"

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            dryRun,
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = "file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}",
        )
    }

    @Test
    fun `Given configure is called with a Project, MavenRepositoryConfiguration and a DryRun flag, it resolves a local URI if a GitRepositoryConfiguration was given and uses the WorkingDirectory over DryRun`() {
        // Given
        val dryRun = true
        val configuration = GitRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = fixture(),
            password = fixture(),
            gitWorkDirectory = fixture(),
        )
        val rootBuildDir = "somewhere"

        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val publishingExtension: PublishingExtension = mockk()
        val repositoryContainer: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val url = slot<String>()

        every { project.extensions } returns extensions
        every { project.rootProject.buildDir } returns File("somewhere")
        every { publishingExtension.repositories } returns repositoryContainer

        every { repository.setUrl(capture(url)) } just Runs

        invokeGradleAction(
            { probe -> extensions.configure(PublishingExtension::class.java, probe) },
            publishingExtension,
        )
        invokeGradleAction(
            { probe -> publishingExtension.repositories(probe) },
            repositoryContainer,
            mockk(),
        )
        invokeGradleAction(
            { probe -> repositoryContainer.maven(probe) },
            repository,
            mockk(),
        )

        // When
        MavenRepository.configure(
            project,
            configuration,
            dryRun,
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = "file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}",
        )
    }

    /*@Test
    fun `Given configure is called with a Project, MavenRepositoryConfiguration and a DryRun flag, it replaces the given URL with a location in the RootProjects build directory if DryRun is false and UseGit is true`() {
        // Given
        val dryRun = false
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
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
        MavenRepository.configure(
            project,
            configuration,
            dryRun
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = ""//""file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}"
        )
    }

    @Test
    fun `Given configure is called with a Project, MavenRepositoryConfiguration and a DryRun flag, it replaces the given URL with a location in the RootProjects build directory if DryRun is true and UseGit is true`() {
        // Given
        val dryRun = true
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
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
        MavenRepository.configure(
            project,
            configuration,
            dryRun
        )

        // Then
        assertEquals(
            actual = url.captured,
            expected = ""//""file://${File(rootBuildDir).absolutePath}/${configuration.name}/${configuration.gitWorkDirectory}"
        )
    }*/
}
