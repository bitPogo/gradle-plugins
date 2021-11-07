/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
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
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.publishing.PublishingError
import tech.antibytes.gradle.publishing.invokeGradleAction
import tech.antibytes.gradle.publishing.publicApi.RegistryConfiguration
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GitRepositorySpec {
    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        mockkObject(GitActions)
    }

    @After
    fun tearDown() {
        unmockkObject(GitActions)
    }

    @Test
    fun `It fulfils GitRepository`() {
        val repository: Any = GitRepository

        assertTrue(repository is GitContract.GitRepository)
    }

    @Test
    fun `Given configureCloneTask is called with a Project and a RegistryConfiguration it does nothing if useGit is false`() {
        // Given
        val project: Project = mockk()
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = false,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = ""
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
    fun `Given configureCloneTasks is called with a Project and a RegistryConfiguration it adds a clone task`() {
        // Given
        val name: String = fixture()

        val project: Project = mockk()
        val configuration = RegistryConfiguration(
            name = name,
            useGit = true,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = ""
        )

        val taskContainer: TaskContainer = mockk()
        val task: Task = mockk()

        every { project.tasks } returns taskContainer
        every { taskContainer.create(any<String>(), any<Closure<*>>()) } returns mockk()

        invokeGradleAction(
            { probe -> taskContainer.create("clone${name.capitalize()}", probe) },
            task,
            mockk()
        )
        invokeGradleAction(
            { probe -> taskContainer.create("push${name.capitalize()}", probe) },
            mockk<Task>(relaxed = true),
            mockk()
        )
        invokeGradleAction(
            { probe -> task.doLast(probe) },
            task,
            mockk()
        )

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
    fun `Given configurePushTask is called with a Project, RegistryConfiguration, Version and a DryRunFlag, it does nothing if useGit is false`() {
        // Given
        val useGit = false
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = RegistryConfiguration(
            name = fixture(),
            useGit = useGit,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = ""
        )

        // When
        GitRepository.configurePushTask(
            project,
            configuration,
            version,
            dryRun
        )

        // Then
        verify(exactly = 0) { project.tasks }
    }

    @Test
    fun `Given configureCloneTasks is called with a Project, RegistryConfiguration, Version and a DryRunFlag it adds a push task`() {
        // Given
        val name: String = fixture()
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = RegistryConfiguration(
            name = name,
            useGit = true,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = ""
        )

        val taskContainer: TaskContainer = mockk(relaxed = true)
        val task: Task = mockk()

        every { project.tasks } returns taskContainer

        invokeGradleAction(
            { probe -> taskContainer.create("push${name.capitalize()}", probe) },
            task,
            mockk()
        )
        invokeGradleAction(
            { probe -> taskContainer.create("clone${name.capitalize()}", probe) },
            mockk<Task>(relaxed = true),
            mockk()
        )
        invokeGradleAction(
            { probe -> task.doLast(probe) },
            task,
            mockk()
        )

        every {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun
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
                dryRun
            )
        }
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration, Version and a DryRunFlag, it adds a push task, which fails if the GitAction returns false`() {
        // Given
        val name: String = fixture()
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = RegistryConfiguration(
            name = name,
            useGit = true,
            gitWorkDirectory = fixture(),
            url = fixture(),
            username = "",
            password = ""
        )

        val taskContainer: TaskContainer = mockk(relaxed = true)
        val task: Task = mockk()

        every { project.tasks } returns taskContainer

        invokeGradleAction(
            { probe -> taskContainer.create("push${name.capitalize()}", probe) },
            task,
            mockk()
        )
        invokeGradleAction(
            { probe -> taskContainer.create("clone${name.capitalize()}", probe) },
            mockk<Task>(relaxed = true),
            mockk()
        )
        invokeGradleAction(
            { probe -> task.doLast(probe) },
            task,
            mockk()
        )

        every {
            task.dependsOn(
                "publishAllPublicationsTo${configuration.name.capitalize()}PackagesRepository"
            )
        } returns task

        every {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun
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
            expected = "Something went wrong while pushing, please manually check the repository."
        )
    }
}
