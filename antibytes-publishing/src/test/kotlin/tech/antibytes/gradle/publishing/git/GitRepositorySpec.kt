/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.git

import com.appmattus.kotlinfixture.kotlinFixture
import groovy.lang.Closure
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingError
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.test.invokeGradleAction

class GitRepositorySpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setup() {
        mockkObject(GitActions)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(GitActions)
    }

    @Test
    fun `It fulfils GitRepository`() {
        val repository: Any = GitRepository

        assertTrue(repository is PublisherContract.GitRepository)
    }

    @Test
    fun `Given configureCloneTask is called with a Project and a GitRepositoryConfiguration it does nothing if the given configuration is not a GitRepositoryConfiguration`() {
        // Given
        val project: Project = mockk()
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = "",
            password = "",
        )

        // When
        GitRepository.configureCloneTask(
            project,
            configuration,
        )

        // Then
        verify(exactly = 0) { project.tasks }
    }

    @Test
    fun `Given configureCloneTasks is called with a Project and a GitRepositoryConfiguration it adds a clone task`() {
        // Given
        val name: String = fixture()

        val project: Project = mockk()
        val configuration = GitRepositoryConfiguration(
            name = name,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = "",
        )

        val taskContainer: TaskContainer = mockk()
        val task: Task = mockk()

        every { project.tasks } returns taskContainer
        every { taskContainer.create(any<String>(), any<Closure<*>>()) } returns mockk()

        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            taskContainer.create("clone${name.capitalize()}", probe)
        }
        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            task.doLast(probe)
        }

        every { GitActions.checkout(project, configuration) } just Runs

        // When
        GitRepository.configureCloneTask(
            project,
            configuration,
        )

        // Then
        verify(exactly = 1) { GitActions.checkout(project, configuration) }
    }

    @Test
    fun `Given configurePushTask is called with a Project, GitRepositoryConfiguration, Version and a DryRunFlag, it does nothing if the given configuration is not a GitRepositoryConfiguration`() {
        // Given
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = MavenRepositoryConfiguration(
            name = fixture(),
            url = fixture(),
            username = "",
            password = "",
        )

        // When
        GitRepository.configurePushTask(
            project,
            configuration,
            version,
            dryRun,
        )

        // Then
        verify(exactly = 0) { project.tasks }
    }

    @Test
    fun `Given configurePushTask is called with a Project, GitRepositoryConfiguration, Version and a DryRunFlag it adds a push task`() {
        // Given
        val name: String = fixture()
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = GitRepositoryConfiguration(
            name = name,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = "",
        )

        val taskContainer: TaskContainer = mockk(relaxed = true)
        val task: Task = mockk()

        every { project.tasks } returns taskContainer

        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            taskContainer.create("push${name.capitalize()}", probe)
        }
        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            task.doLast(probe)
        }

        every {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun,
            )
        } returns true

        // When
        GitRepository.configurePushTask(
            project,
            configuration,
            version,
            dryRun,
        )

        // Then
        verify(exactly = 1) {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun,
            )
        }
    }

    @Test
    fun `Given configurePushTask is called with a Project, GitRepositoryConfiguration, Version, a DryRunFlag and a Suffix it adds a push task`() {
        // Given
        val name: String = fixture()
        val suffix: String = fixture()
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = GitRepositoryConfiguration(
            name = name,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = "",
        )

        val taskContainer: TaskContainer = mockk(relaxed = true)
        val task: Task = mockk()

        every { project.tasks } returns taskContainer

        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            taskContainer.create("push${name.capitalize()}$suffix", probe)
        }
        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            task.doLast(probe)
        }

        every {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun,
            )
        } returns true

        // When
        GitRepository.configurePushTask(
            project,
            configuration,
            version,
            dryRun,
            suffix,
        )

        // Then
        verify(exactly = 1) {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun,
            )
        }
    }

    @Test
    fun `Given configure is called with a Project, GitRepositoryConfiguration, Version and a DryRunFlag, it adds a push task, which fails if the GitAction returns false`() {
        // Given
        val name: String = fixture()
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = GitRepositoryConfiguration(
            name = name,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = "",
        )

        val taskContainer: TaskContainer = mockk(relaxed = true)
        val task: Task = mockk()

        every { project.tasks } returns taskContainer

        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            taskContainer.create("push${name.capitalize()}", probe)
        }
        invokeGradleAction(
            task,
            mockk(),
        ) { probe ->
            task.doLast(probe)
        }

        every {
            task.dependsOn(
                "publishAllPublicationsTo${configuration.name.capitalize()}PackagesRepository",
            )
        } returns task

        every {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun,
            )
        } returns false

        // Then
        val error = assertFailsWith<PublishingError.GitRejectedCommitError> {
            // When
            GitRepository.configurePushTask(
                project,
                configuration,
                version,
                dryRun,
            )
        }

        assertEquals(
            actual = error.message,
            expected = "Something went wrong while pushing, please manually check the repository.",
        )
    }
}
