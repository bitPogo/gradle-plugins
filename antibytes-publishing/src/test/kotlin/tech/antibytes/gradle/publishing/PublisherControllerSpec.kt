/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import io.mockk.verifyOrder
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.publishing.PublishingApiContract.PackageConfiguration
import tech.antibytes.gradle.publishing.PublishingApiContract.VersioningConfiguration
import tech.antibytes.gradle.publishing.api.RegistryConfiguration
import tech.antibytes.gradle.publishing.api.VersionInfo
import tech.antibytes.gradle.publishing.git.GitRepository
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRegistry
import kotlin.test.assertTrue

class PublisherControllerSpec {
    private val fixture = kotlinFixture()
    private val registryTestConfig = RegistryConfiguration(
        username = "",
        password = "",
        name = "",
        url = "",
        useGit = false,
        gitWorkDirectory = ""
    )

    @Before
    fun setUp() {
        mockkObject(Versioning)
        mockkObject(MavenPublisher)
        mockkObject(MavenRegistry)
        mockkObject(GitRepository)
    }

    @After
    fun tearDown() {
        unmockkObject(Versioning)
        unmockkObject(MavenPublisher)
        unmockkObject(MavenRegistry)
        unmockkObject(GitRepository)
    }

    @Test
    fun `It fulfils PublisherController`() {
        val controller: Any = PublisherController

        assertTrue(controller is PublishingContract.PublisherController)
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no PackageConfiguration was given`() {
        // Given
        val name: String = fixture()

        val project: Project = mockk()
        val config = TestConfig(
            registryConfiguration = setOf(mockk()),
            packageConfiguration = null,
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
        )

        every { project.name } returns name
        every { Versioning.versionName(any(), any()) } returns fixture()

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(
            project,
            config
        )

        // Then
        verify(exactly = 0) { Versioning.versionName(any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if the project was excludes`() {
        // Given
        val name: String = fixture()

        val project: Project = mockk()
        val config = TestConfig(
            registryConfiguration = setOf(mockk()),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
        )

        every { project.name } returns name
        every { Versioning.versionName(any(), any()) } returns fixture()

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(
            project,
            config
        )

        // Then
        verify(exactly = 0) { Versioning.versionName(any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it adds a version Task`() {
        // Given
        val project: Project = mockk()
        val dryRun: Boolean = fixture()

        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            registryConfiguration = setOf(mockk(relaxed = true)),
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
        )

        val version: String = fixture()
        val tasks: TaskContainer = mockk()
        val versionTask: Task = mockk()
        val info = VersionInfo(
            name = fixture(),
            details = mockk()
        )

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()
        every { tasks.named(any(), any<Action<Task>>()) } returns mockk()

        every { info.details.branchName } returns fixture()
        every { info.details.gitHashFull } returns fixture()
        every { info.details.gitHash } returns fixture()
        every { info.details.lastTag } returns fixture()
        every { info.details.commitDistance } returns fixture()
        every { info.details.isCleanTag } returns fixture()
        every { info.details.version } returns fixture()

        every { Versioning.versionName(project, versioningConfiguration) } returns version
        every { MavenPublisher.configure(project, packageConfiguration, version) } just Runs
        every { MavenRegistry.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } just Runs
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } just Runs

        every { versionTask.group = "Versioning" } just Runs
        every { versionTask.description = any() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        invokeGradleAction(
            { probe -> tasks.create("versionInfo", probe) },
            versionTask,
            mockk()
        )

        invokeGradleAction(
            { probe -> versionTask.doLast(probe) },
            versionTask,
            mockk()
        )

        every {
            Versioning.versionInfo(
                project,
                config.versioning
            )
        } returns info

        // When
        PublisherController.configure(
            project,
            config
        )

        // Then
        verify(exactly = 1) { versionTask.group = "Versioning" }
        verify(exactly = 1) { versionTask.description = "Displays the current version" }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no registryConfiguration was given`() {
        // Given
        val project: Project = mockk()
        val config = TestConfig(
            registryConfiguration = emptySet(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = emptySet(),
            versioning = mockk(),
        )

        every { project.name } returns fixture()
        every { project.tasks } returns mockk()
        every { Versioning.versionName(any(), any()) } returns fixture()

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(
            project,
            config
        )

        // Then
        verify(exactly = 0) { Versioning.versionName(any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it distributes the configurations`() {
        // Given
        val project: Project = mockk()
        val registry1 = registryTestConfig.copy(name = "a")
        val registry2 = registryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()

        val registryConfiguration: Set<PublishingApiContract.RegistryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            registryConfiguration = registryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
        )

        val version: String = fixture()
        val tasks: TaskContainer = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()
        every { tasks.named(any(), any<Action<Task>>()) } returns mockk()

        every { Versioning.versionName(project, versioningConfiguration) } returns version
        every { MavenPublisher.configure(project, packageConfiguration, version) } just Runs
        every { MavenRegistry.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } just Runs
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        // When
        PublisherController.configure(
            project,
            config
        )

        // Then
        verify(exactly = 1) { Versioning.versionName(project, versioningConfiguration) }
        verify(exactly = 1) { MavenPublisher.configure(project, packageConfiguration, version) }

        verify(exactly = 1) {
            MavenRegistry.configure(
                project,
                registry1,
                dryRun
            )
        }

        verify(exactly = 1) {
            GitRepository.configureCloneTask(
                project,
                registry1
            )
        }

        verify(exactly = 1) {
            GitRepository.configurePushTask(
                project,
                registry1,
                version,
                dryRun
            )
        }

        verify(exactly = 1) {
            MavenRegistry.configure(
                project,
                registry2,
                dryRun
            )
        }

        verify(exactly = 1) {
            GitRepository.configureCloneTask(
                project,
                registry2
            )
        }

        verify(exactly = 1) {
            GitRepository.configurePushTask(
                project,
                registry2,
                version,
                dryRun
            )
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it adds a publishing Task`() {
        // Given
        val project: Project = mockk()
        val registry1 = registryTestConfig.copy(name = "a")
        val registry2 = registryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()

        val registryConfiguration: Set<RegistryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            registryConfiguration = registryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
        )

        val version: String = fixture()
        val tasks: TaskContainer = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()
        every { tasks.named(any(), any<Action<Task>>()) } returns mockk()

        every { Versioning.versionName(project, versioningConfiguration) } returns version
        every { MavenPublisher.configure(project, packageConfiguration, version) } just Runs
        every { MavenRegistry.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } just Runs
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } just Runs

        every { publishingTask.group = "Publishing" } just Runs
        every { publishingTask.description = any() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        invokeGradleAction(
            { probe -> tasks.create("publish${registry1.name.capitalize()}", probe) },
            publishingTask,
            mockk()
        )
        invokeGradleAction(
            { probe -> tasks.create("publish${registry2.name.capitalize()}", probe) },
            publishingTask,
            mockk()
        )

        // When
        PublisherController.configure(
            project,
            config
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
        val registry1 = registryTestConfig.copy(name = "a")
        val registry2 = registryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()

        val registryConfiguration: Set<PublishingApiContract.RegistryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            registryConfiguration = registryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
        )

        val version: String = fixture()
        val tasks: TaskContainer = mockk()

        val mavenTask: Task = mockk()
        val gitPushTask: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()

        every { Versioning.versionName(project, versioningConfiguration) } returns version
        every { MavenPublisher.configure(project, packageConfiguration, version) } just Runs
        every { MavenRegistry.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } just Runs
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } just Runs

        every { mavenTask.dependsOn(any()) } returns mavenTask

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        invokeGradleAction(
            { probe -> tasks.named("publishAllPublicationsTo${registry1.name.capitalize()}Repository", probe) },
            mavenTask,
            mockk<TaskProvider<Task>>()
        )
        invokeGradleAction(
            { probe -> tasks.named("publishAllPublicationsTo${registry2.name.capitalize()}Repository", probe) },
            mavenTask,
            mockk<TaskProvider<Task>>()
        )

        invokeGradleAction(
            { probe -> tasks.named("push${registry1.name.capitalize()}", probe) },
            gitPushTask,
            mockk<TaskProvider<Task>>()
        )
        invokeGradleAction(
            { probe -> tasks.named("push${registry2.name.capitalize()}", probe) },
            gitPushTask,
            mockk<TaskProvider<Task>>()
        )

        invokeGradleAction(
            { probe -> tasks.named("publish${registry1.name.capitalize()}", probe) },
            publishingTask,
            mockk<TaskProvider<Task>>()
        )
        invokeGradleAction(
            { probe -> tasks.named("publish${registry2.name.capitalize()}", probe) },
            publishingTask,
            mockk<TaskProvider<Task>>()
        )

        every { mavenTask.dependsOn(any()) } returns mavenTask
        every { gitPushTask.dependsOn(any()) } returns gitPushTask
        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherController.configure(
            project,
            config
        )

        // Then
        verify(exactly = 1) { mavenTask.dependsOn("clone${registry1.name.capitalize()}") }
        verify(exactly = 1) { mavenTask.dependsOn("clone${registry2.name.capitalize()}") }

        verify(exactly = 1) { gitPushTask.dependsOn("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { gitPushTask.dependsOn("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { publishingTask.dependsOn("push${registry1.name.capitalize()}") }
        verify(exactly = 1) { publishingTask.dependsOn("push${registry2.name.capitalize()}") }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it runs the tasks in order`() {
        // Given
        val project: Project = mockk()
        val registry1 = registryTestConfig.copy(name = "a")
        val registry2 = registryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()

        val registryConfiguration: Set<PublishingApiContract.RegistryConfiguration> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            registryConfiguration = registryConfiguration,
            packageConfiguration = packageConfiguration,
            dryRun = dryRun,
            excludeProjects = emptySet(),
            versioning = versioningConfiguration,
        )

        val version: String = fixture()
        val tasks: TaskContainer = mockk()

        val mavenTask: Task = mockk()
        val gitPushTask: Task = mockk()
        val publishingTask: Task = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()
        every { tasks.named(any(), any<Action<Task>>()) } returns mockk()

        every { Versioning.versionName(project, versioningConfiguration) } returns version
        every { MavenPublisher.configure(project, packageConfiguration, version) } just Runs
        every { MavenRegistry.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } just Runs
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } just Runs

        every { mavenTask.dependsOn(any()) } returns mavenTask

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk()
        )

        every { mavenTask.dependsOn(any()) } returns mavenTask
        every { gitPushTask.dependsOn(any()) } returns gitPushTask
        every { publishingTask.dependsOn(any()) } returns publishingTask

        // When
        PublisherController.configure(
            project,
            config
        )

        // Then
        verifyOrder {
            Versioning.versionName(
                project,
                config.versioning
            )

            MavenPublisher.configure(
                project,
                packageConfiguration,
                version
            )

            MavenRegistry.configure(project, registry1, dryRun)

            GitRepository.configureCloneTask(project, registry1)
            GitRepository.configurePushTask(project, registry1, version, dryRun)

            tasks.create("publish${registry1.name.capitalize()}", any<Action<Task>>())
            tasks.named(
                "publishAllPublicationsTo${registry1.name.capitalize()}Repository",
                any<Action<Task>>()
            )
            tasks.named("push${registry1.name.capitalize()}", any<Action<Task>>())
            tasks.named("publish${registry1.name.capitalize()}", any<Action<Task>>())

            MavenRegistry.configure(project, registry2, dryRun)

            GitRepository.configureCloneTask(project, registry2)
            GitRepository.configurePushTask(project, registry2, version, dryRun)

            tasks.create("publish${registry2.name.capitalize()}", any<Action<Task>>())
            tasks.named(
                "publishAllPublicationsTo${registry2.name.capitalize()}Repository",
                any<Action<Task>>()
            )
            tasks.named("push${registry2.name.capitalize()}", any<Action<Task>>())
            tasks.named("publish${registry2.name.capitalize()}", any<Action<Task>>())
        }
    }
}

private data class TestConfig(
    override var registryConfiguration: Set<PublishingApiContract.RegistryConfiguration>,
    override var packageConfiguration: PackageConfiguration?,
    override var dryRun: Boolean,
    override var excludeProjects: Set<String>,
    override var versioning: VersioningConfiguration
) : PublishingContract.PublishingPluginConfiguration
