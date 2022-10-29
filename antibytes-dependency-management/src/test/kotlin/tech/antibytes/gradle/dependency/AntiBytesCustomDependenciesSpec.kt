/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.artifacts.repositories.RepositoryContentDescriptor
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.dependency.DependencyContract.Credentials
import tech.antibytes.gradle.test.invokeGradleAction

class AntiBytesCustomDependenciesSpec {
    @Test
    fun `Given a RepositoryHandler with addCustomRepositories, it adds the given Repositories`() {
        // Given
        val handler: RepositoryHandler = mockk()
        val mavenRepository: MavenArtifactRepository = mockk(relaxed = true)
        val repository = CustomRepository("test")

        invokeGradleAction(
            { probe -> handler.maven(probe) },
            mavenRepository,
            mavenRepository,
        )

        // When
        handler.addCustomRepositories(listOf(repository, repository))

        // Then
        verify(exactly = 2) { mavenRepository.setUrl("test") }
    }

    @Test
    fun `Given a RepositoryHandler with addCustomRepositories, it adds the Antibytes Repositories with the presetted groups`() {
        // Given
        val handler: RepositoryHandler = mockk()
        val mavenRepository: MavenArtifactRepository = mockk(relaxed = true)
        val content: RepositoryContentDescriptor = mockk(relaxed = true)
        val groups = listOf("1", "2", "3")
        val repository = CustomRepository(
            url = "test",
            groupIds = groups,
        )

        invokeGradleAction(
            { probe -> handler.maven(probe) },
            mavenRepository,
            mavenRepository,
        )

        invokeGradleAction(
            { probe -> mavenRepository.content(probe) },
            content,
            content,
        )

        // When
        handler.addCustomRepositories(listOf(repository, repository))

        // Then
        groups.forEach { group ->
            verify(exactly = 2) { content.includeGroup(group) }
        }
    }

    @Test
    fun `Given a RepositoryHandler with addCustomRepositories, it adds the Antibytes Repositories with the presetted credentials`() {
        // Given
        val handler: RepositoryHandler = mockk()
        val mavenRepository: MavenArtifactRepository = mockk(relaxed = true)
        val mavenCredentials: PasswordCredentials = mockk(relaxed = true)
        val credentials = Credentials(
            username = "Test",
            password = "safe",
        )
        val repository = CustomRepository(
            url = "test",
            credentials = credentials,
        )

        invokeGradleAction(
            { probe -> handler.maven(probe) },
            mavenRepository,
            mavenRepository,
        )

        invokeGradleAction(
            { probe -> mavenRepository.credentials(probe) },
            mavenCredentials,
            mavenCredentials,
        )

        // When
        handler.addCustomRepositories(listOf(repository, repository))

        // Then
        verify(exactly = 2) { mavenCredentials.username = credentials.username }
        verify(exactly = 2) { mavenCredentials.password = credentials.password }
    }
}
