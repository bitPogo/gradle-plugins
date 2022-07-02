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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.dependency.AntiBytesCustomDependencies.addCustomRepositories
import tech.antibytes.gradle.test.invokeGradleAction

class AntiBytesCustomDependenciesSpec {
    @AfterEach
    fun tearDown() {
        AntiBytesCustomDependencies.githubGroups = emptyList()
        AntiBytesCustomDependencies.credentials = null
    }

    @Test
    fun `Given a RepositoryHandler with addCustomRepositories, it adds the Antibytes Repositories`() {
        // Given
        val handler: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)

        invokeGradleAction(
            { probe -> handler.maven(probe) },
            repository,
            repository,
        )

        // When
        handler.addCustomRepositories()

        // Then
        verify(exactly = 1) { repository.setUrl("https://raw.github.com/bitPogo/maven-dev/main/dev") }
        verify(exactly = 1) { repository.setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots") }
    }

    @Test
    fun `Given a RepositoryHandler with addCustomRepositories, it adds the Antibytes Repositories with the presetted groups`() {
        // Given
        val handler: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val content: RepositoryContentDescriptor = mockk(relaxed = true)
        val groups = listOf("1", "2", "3")

        invokeGradleAction(
            { probe -> handler.maven(probe) },
            repository,
            repository,
        )

        invokeGradleAction(
            { probe -> repository.content(probe) },
            content,
            content,
        )

        // When
        AntiBytesCustomDependencies.githubGroups = groups
        handler.addCustomRepositories()

        // Then
        groups.forEach { group ->
            verify(exactly = 2) { content.includeGroup(group) }
        }
    }

    @Test
    fun `Given a RepositoryHandler with addCustomRepositories, it adds the Antibytes Repositories with the presetted credentials`() {
        // Given
        val handler: RepositoryHandler = mockk()
        val repository: MavenArtifactRepository = mockk(relaxed = true)
        val mavenCredentials: PasswordCredentials = mockk(relaxed = true)
        val credentials = DependencyContract.Credentials(
            username = "Test",
            password = "safe",
        )

        invokeGradleAction(
            { probe -> handler.maven(probe) },
            repository,
            repository,
        )

        invokeGradleAction(
            { probe -> repository.credentials(probe) },
            mavenCredentials,
            mavenCredentials,
        )

        // When
        AntiBytesCustomDependencies.credentials = credentials
        handler.addCustomRepositories()

        // Then
        verify(exactly = 2) { mavenCredentials.username = credentials.username }
        verify(exactly = 2) { mavenCredentials.password = credentials.password }
    }
}
