/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
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
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.Versioning
import tech.antibytes.gradle.publishing.api.VersionInfo
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertTrue

class PublisherControllerSpec {
    private val fixture = kotlinFixture()

    @Before
    fun setUp() {
        mockkObject(Versioning)
        mockkObject(PublisherRootProjectController)
        mockkObject(PublisherSubProjectController)
        mockkObject(PublisherStandaloneController)
    }

    @After
    fun tearDown() {
        unmockkObject(Versioning)
        unmockkObject(PublisherRootProjectController)
        unmockkObject(PublisherSubProjectController)
        unmockkObject(PublisherStandaloneController)
    }

    @Test
    fun `It fulfils PublisherController`() {
        val controller: Any = PublisherController

        assertTrue(controller is PublishingContract.PublisherController)
    }

    @Test
    fun `Given configure is called with a Project, Configuration and a IsRoot Flag, it adds a Versioning task to the Project root if it does not exists`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            registryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
            standalone = true
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()
        val versioningTask: Task = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName("versionInfo") } returns null

        every { versioningTask.group = any() } just Runs
        every { versioningTask.description = any() } just Runs
        every { Versioning.versionInfo(project, config.versioning) } returns VersionInfo(fixture(), mockk(relaxed = true))

        every { PublisherStandaloneController.configure(any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        invokeGradleAction(
            { probe -> tasks.create("versionInfo", probe) },
            versioningTask,
            mockk()
        )

        invokeGradleAction(
            { probe -> versioningTask.doLast(probe) },
            versioningTask,
            mockk()
        )

        // When
        PublisherController.configure(project, config)

        // Then
        verify(exactly = 1) { versioningTask.group = "Versioning" }
        verify(exactly = 1) { versioningTask.description = "Displays the current version" }
        verify(exactly = 1) { Versioning.versionInfo(project, config.versioning) }
    }

    @Test
    fun `Given configure is called with a Project, Configuration and a IsRoot Flag, it will not a Versioning task to the Project root if the task already exists`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            registryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
            standalone = true
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()
        val versioningTask: Task = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName("versionInfo") } returns mockk()

        every { versioningTask.group = any() } just Runs
        every { versioningTask.description = any() } just Runs
        every { Versioning.versionInfo(project, config.versioning) } returns mockk()

        every { PublisherStandaloneController.configure(any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        invokeGradleAction(
            { probe -> tasks.create("versionInfo", probe) },
            versioningTask,
            mockk()
        )

        invokeGradleAction(
            { probe -> versioningTask.doLast(probe) },
            versioningTask,
            mockk()
        )

        // When
        PublisherController.configure(project, config)

        // Then
        verify(exactly = 0) { versioningTask.group = "Versioning" }
        verify(exactly = 0) { versioningTask.description = "Displays the current version" }
        verify(exactly = 0) { Versioning.versionInfo(project, config.versioning) }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration the Project if it is excludes`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            registryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
            standalone = true
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()

        every { PublisherStandaloneController.configure(any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(project, config)

        // Then
        verify(exactly = 0) { PublisherStandaloneController.configure(any(), any()) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the Standalone Configuration, if it is configured as Standalone`() {
        // Given
        val config = TestConfig(
            registryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(),
            versioning = mockk(),
            standalone = true
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()

        every { PublisherStandaloneController.configure(project, config) } just Runs
        every { PublisherRootProjectController.configure(any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(project, config)

        // Then
        verify(exactly = 1) { PublisherStandaloneController.configure(project, config) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), any()) }
        verify(exactly = 0) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the SubProjectPublisher, if it is configured as non Standalone and the target is not root`() {
        // Given
        val config = TestConfig(
            registryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(),
            versioning = mockk(),
            standalone = false
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()

        every { PublisherStandaloneController.configure(any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any()) } just Runs
        every { PublisherSubProjectController.configure(project, config) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(project, config)

        // Then
        verify(exactly = 0) { PublisherStandaloneController.configure(any(), any()) }
        verify(exactly = 1) { PublisherSubProjectController.configure(project, config) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), any()) }
        verify(exactly = 0) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it sets up the evaluation dependencies if the target is root`() {
        // Given
        val config = TestConfig(
            registryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(),
            versioning = mockk(),
            standalone = false
        )

        val project: Project = mockk()
        val tasks: TaskContainer = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns project
        every { project.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()
        every { project.evaluationDependsOnChildren() } just Runs

        every { PublisherStandaloneController.configure(any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(project, config)

        // Then
        verify(exactly = 1) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the RootProjectPublisher if the target is root and it is configured as non Standalone `() {
        // Given
        val config = TestConfig(
            registryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(),
            versioning = mockk(),
            standalone = false
        )

        val project: Project = mockk()
        val tasks: TaskContainer = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns project
        every { project.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()
        every { project.evaluationDependsOnChildren() } just Runs

        every { PublisherStandaloneController.configure(any(), any()) } just Runs
        every { PublisherRootProjectController.configure(project, config) } just Runs
        every { PublisherSubProjectController.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(project, config)

        // Then
        verify(exactly = 0) { PublisherStandaloneController.configure(any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), any()) }
        verify(exactly = 1) { PublisherRootProjectController.configure(project, config) }
    }
}
