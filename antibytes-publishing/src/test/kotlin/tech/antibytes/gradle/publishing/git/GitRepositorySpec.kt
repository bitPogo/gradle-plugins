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
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.test.invokeGradleAction
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
        GitRepository.configure(
            project,
            listOf(configuration),
            fixture(),
            false
        )

        // Then
        verify(exactly = 0) { project.tasks }
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it adds a clone task`() {
        // Given
        val name: String = fixture()

        val project: Project = mockk()
        val configuration = TestConfiguration(
            name = name,
            useGit = true,
            gitWorkDirectory = fixture(),
            url = fixture()
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
        every { GitActions.push(any(), any(), any(), any()) } returns fixture()

        // When
        GitRepository.configure(
            project,
            listOf(configuration),
            fixture(),
            fixture(),
        )

        // Then
        verify(exactly = 1) { GitActions.checkout(project, configuration) }
    }

    @Test
    fun `Given configure is called with a Project, RegistryConfiguration and a DryRun flag, it adds a push task`() {
        // Given
        val name: String = fixture()
        val version: String = fixture()
        val dryRun: Boolean = fixture()

        val project: Project = mockk()
        val configuration = TestConfiguration(
            name = name,
            useGit = true,
            gitWorkDirectory = fixture(),
            url = fixture()
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

        every { GitActions.checkout(any(), any()) } just Runs
        every {
            GitActions.push(
                project,
                configuration,
                version,
                dryRun
            )
        } returns fixture()

        // When
        GitRepository.configure(
            project,
            listOf(configuration),
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
        verify(exactly = 1) {
            task.dependsOn(
                "publishAllPublicationsTo${configuration.name.capitalize()}PackagesRepository"
            )
        }
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
