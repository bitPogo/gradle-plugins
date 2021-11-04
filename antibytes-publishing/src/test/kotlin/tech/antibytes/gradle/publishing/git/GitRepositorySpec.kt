/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.junit.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import kotlin.test.assertTrue

class GitRepositorySpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils GitRepository`() {
        val repository: Any = GitRepository

        assertTrue(repository is GitContract.GitRepository)
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it does nothing if useGit is false`() {
        // Given
        val project: Project = mockk()
        val configuration = TestConfiguration(
            name = fixture(),
            useGit = false,
            gitWorkDirectory = fixture(),
            url = fixture()
        )

        // When
        GitRepository.configure(project, listOf(configuration), false)

        // Then
        verify(exactly = 0) { project.tasks }
    }
}

private data class TestConfiguration(
    override val name: String,
    override val useGit: Boolean,
    override val gitWorkDirectory: String,
    override val url: String,
    override val username: String? = null,
    override val password: String? = null
) : PublishingApiContract.RegistryConfiguration
