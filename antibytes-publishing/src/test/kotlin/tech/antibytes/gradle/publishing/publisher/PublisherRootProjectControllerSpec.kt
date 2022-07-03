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
import io.mockk.verifyOrder
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.RepositoryConfiguration
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.api.GitRepositoryConfiguration
import tech.antibytes.gradle.publishing.api.MavenRepositoryConfiguration
import tech.antibytes.gradle.publishing.git.GitRepository
import tech.antibytes.gradle.publishing.maven.MavenRepository
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

class PublisherRootProjectControllerSpec {
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
        mockkObject(MavenRepository)
        mockkObject(GitRepository)
    }

    @AfterEach
    fun tearDown() {
        mockkObject(MavenRepository)
        unmockkObject(GitRepository)
    }

    @Test
    fun `It fulfils PublisherController`() {
        val controller: Any = PublisherRootProjectController

        assertTrue(controller is PublishingContract.PublisherController)
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no registryConfiguration was given`() {
        // Given
        val project: Project = mockk()
        val config = TestConfig(
            repositoryConfiguration = emptySet(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = emptySet(),
            versioning = mockk(),
            standalone = true,
        )

        every { project.name } returns fixture()
        every { project.tasks } returns mockk()

        // When
        PublisherRootProjectController.configure(
            project,
            "version",
            null,
            config,
        )

        // Then
        verify(exactly = 0) { GitRepository.configureCloneTask(project, any()) }
        verify(exactly = 0) { MavenRepository.configure(project, any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it distributes the configurations`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val tasks: TaskContainer = mockk()
        val task: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(project)

        every { tasks.create(any(), any<Action<Task>>()) } returns task
        every { tasks.findByName(any()) } returns task

        every { task.dependsOn(any()) } returns task

        every { GitRepository.configureCloneTask(project, any()) } returns task
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns task

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) {
            GitRepository.configureCloneTask(
                project,
                registry1,
            )
        }

        verify(exactly = 1) {
            GitRepository.configurePushTask(
                project,
                registry1,
                version,
                dryRun,
            )
        }

        verify(exactly = 1) {
            GitRepository.configureCloneTask(
                project,
                registry2,
            )
        }

        verify(exactly = 1) {
            GitRepository.configurePushTask(
                project,
                registry2,
                version,
                dryRun,
            )
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it adds a publishing Task`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val registryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = registryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val tasks: TaskContainer = mockk()
        val publishingTask: Task = mockk()
        val task: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(project)
        every { publishingTask.dependsOn(any()) } returns publishingTask

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask
        every { tasks.findByName(any()) } returns task
        every { task.dependsOn(any()) } returns task

        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns task
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns task

        every { publishingTask.group = "Publishing" } just Runs
        every { publishingTask.description = any() } just Runs

        invokeGradleAction(
            { probe -> tasks.create("publish${registry1.name.capitalize()}", probe) },
            publishingTask,
            publishingTask,
        )
        invokeGradleAction(
            { probe -> tasks.create("publish${registry2.name.capitalize()}", probe) },
            publishingTask,
            publishingTask,
        )

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 2) { publishingTask.group = "Publishing" }
        verify(exactly = 1) { publishingTask.description = "Publish ${registry1.name.capitalize()}" }
        verify(exactly = 1) { publishingTask.description = "Publish ${registry2.name.capitalize()}" }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        val tasks: TaskContainer = mockk()
        val tasks1: TaskContainer = mockk()
        val tasks2: TaskContainer = mockk()

        val gitCloneTask: Task = mockk()
        val maven1Task: Task = mockk()
        val maven2Task: Task = mockk()
        val maven3Task: Task = mockk()
        val maven4Task: Task = mockk()
        val gitPushTask: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.tasks } returns tasks1
        every { subproject2.tasks } returns tasks2

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every {
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every {
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven3Task
        every {
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven4Task

        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns gitCloneTask
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns gitPushTask

        every { maven1Task.dependsOn(any()) } returns maven1Task
        every { maven2Task.dependsOn(any()) } returns maven2Task
        every { maven3Task.dependsOn(any()) } returns maven3Task
        every { maven4Task.dependsOn(any()) } returns maven4Task

        every { gitPushTask.dependsOn(any()) } returns gitPushTask
        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { maven1Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { maven2Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { maven3Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { maven4Task.dependsOn(gitCloneTask) }

        verify(exactly = 1) { gitPushTask.dependsOn(listOf(maven1Task, maven3Task)) }
        verify(exactly = 1) { gitPushTask.dependsOn(listOf(maven2Task, maven4Task)) }

        verify(exactly = 2) { publishingTask.dependsOn(gitPushTask) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it runs the tasks in order`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        val tasks: TaskContainer = mockk()
        val tasks1: TaskContainer = mockk()
        val tasks2: TaskContainer = mockk()

        val gitCloneTask: Task = mockk()
        val maven1Task: Task = mockk()
        val maven2Task: Task = mockk()
        val maven3Task: Task = mockk()
        val maven4Task: Task = mockk()
        val gitPushTask: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.tasks } returns tasks1
        every { subproject2.tasks } returns tasks2

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every {
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every {
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven3Task
        every {
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven4Task

        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns gitCloneTask
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns gitPushTask

        every { maven1Task.dependsOn(any()) } returns maven1Task
        every { maven2Task.dependsOn(any()) } returns maven2Task
        every { maven3Task.dependsOn(any()) } returns maven3Task
        every { maven4Task.dependsOn(any()) } returns maven4Task

        every { gitPushTask.dependsOn(any()) } returns gitPushTask
        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verifyOrder {
            GitRepository.configureCloneTask(project, registry1)
            GitRepository.configurePushTask(project, registry1, version, dryRun)

            tasks.create("publish${registry1.name.capitalize()}", any<Action<Task>>())
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")

            maven1Task.dependsOn(gitCloneTask)
            maven3Task.dependsOn(gitCloneTask)
            gitPushTask.dependsOn(listOf(maven1Task, maven3Task))
            publishingTask.dependsOn(gitPushTask)

            GitRepository.configureCloneTask(project, registry2)
            GitRepository.configurePushTask(project, registry2, version, dryRun)

            tasks.create("publish${registry2.name.capitalize()}", any<Action<Task>>())

            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")

            maven2Task.dependsOn(gitCloneTask)
            maven4Task.dependsOn(gitCloneTask)
            gitPushTask.dependsOn(listOf(maven2Task, maven4Task))
            publishingTask.dependsOn(gitPushTask)
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies while Git is not in use`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        val tasks: TaskContainer = mockk()
        val tasks1: TaskContainer = mockk()
        val tasks2: TaskContainer = mockk()

        val maven1Task: Task = mockk()
        val maven2Task: Task = mockk()
        val maven3Task: Task = mockk()
        val maven4Task: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.tasks } returns tasks1
        every { subproject2.tasks } returns tasks2

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every {
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every {
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven3Task
        every {
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven4Task

        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns null
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns null

        every { maven1Task.dependsOn(any()) } returns maven1Task
        every { maven2Task.dependsOn(any()) } returns maven2Task
        every { maven3Task.dependsOn(any()) } returns maven3Task
        every { maven4Task.dependsOn(any()) } returns maven4Task

        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { publishingTask.dependsOn(listOf(maven1Task, maven3Task)) }
        verify(exactly = 1) { publishingTask.dependsOn(listOf(maven2Task, maven4Task)) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it runs the tasks in order while Git is not in use`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        val tasks: TaskContainer = mockk()
        val tasks1: TaskContainer = mockk()
        val tasks2: TaskContainer = mockk()

        val maven1Task: Task = mockk()
        val maven2Task: Task = mockk()
        val maven3Task: Task = mockk()
        val maven4Task: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.tasks } returns tasks1
        every { subproject2.tasks } returns tasks2

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every {
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every {
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven3Task
        every {
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven4Task

        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns null
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns null

        every { maven1Task.dependsOn(any()) } returns maven1Task
        every { maven2Task.dependsOn(any()) } returns maven2Task
        every { maven3Task.dependsOn(any()) } returns maven3Task
        every { maven4Task.dependsOn(any()) } returns maven4Task

        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verifyOrder {
            GitRepository.configureCloneTask(project, registry1)
            GitRepository.configurePushTask(project, registry1, version, dryRun)

            tasks.create("publish${registry1.name.capitalize()}", any<Action<Task>>())
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")

            publishingTask.dependsOn(listOf(maven1Task, maven3Task))

            GitRepository.configureCloneTask(project, registry2)
            GitRepository.configurePushTask(project, registry2, version, dryRun)

            tasks.create("publish${registry2.name.capitalize()}", any<Action<Task>>())
            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")

            publishingTask.dependsOn(listOf(maven2Task, maven4Task))
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies and ignores non existing maven tasks()`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        val tasks: TaskContainer = mockk()
        val tasks1: TaskContainer = mockk()
        val tasks2: TaskContainer = mockk()

        val gitCloneTask: Task = mockk()
        val maven1Task: Task = mockk()
        val maven2Task: Task = mockk()
        val gitPushTask: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.tasks } returns tasks1
        every { subproject2.tasks } returns tasks2

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every {
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns null
        every {
            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every {
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns null

        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns gitCloneTask
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns gitPushTask

        every { maven1Task.dependsOn(any()) } returns maven1Task
        every { maven2Task.dependsOn(any()) } returns maven2Task

        every { gitPushTask.dependsOn(any()) } returns gitPushTask
        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { maven1Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { maven2Task.dependsOn(gitCloneTask) }

        verify(exactly = 1) { gitPushTask.dependsOn(listOf(maven1Task)) }
        verify(exactly = 1) { gitPushTask.dependsOn(listOf(maven2Task)) }

        verify(exactly = 2) { publishingTask.dependsOn(gitPushTask) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies and ignores non existing maven tasks, while Git is not in use`() {
        // Given
        val project: Project = mockk()
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoryConfiguration: Set<RepositoryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            repositoryConfiguration = repositoryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
            standalone = true,
        )

        val subproject1: Project = mockk()
        val subproject2: Project = mockk()

        val tasks: TaskContainer = mockk()
        val tasks1: TaskContainer = mockk()
        val tasks2: TaskContainer = mockk()

        val maven1Task: Task = mockk()
        val maven2Task: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { project.subprojects } returns setOf(subproject1, subproject2)

        every { subproject1.tasks } returns tasks1
        every { subproject2.tasks } returns tasks2

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every {
            tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns null
        every {
            tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every {
            tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns null

        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns null
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns null

        every { maven1Task.dependsOn(any()) } returns maven1Task
        every { maven2Task.dependsOn(any()) } returns maven2Task

        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherRootProjectController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks1.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks2.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { publishingTask.dependsOn(listOf(maven1Task)) }
        verify(exactly = 1) { publishingTask.dependsOn(listOf(maven2Task)) }
    }
}
