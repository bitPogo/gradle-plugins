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
import org.gradle.api.provider.MapProperty
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
import tech.antibytes.gradle.publishing.maven.MavenPublisher
import tech.antibytes.gradle.publishing.maven.MavenRepository
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeMapProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeSetProperty
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.util.capitalize
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

@Suppress("UNCHECKED_CAST")
class PublisherStandaloneControllerSpec {
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
        mockkObject(
            MavenPublisher,
            MavenRepository,
            GitRepository,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            MavenPublisher,
            MavenRepository,
            GitRepository,
        )
    }

    @Test
    fun `It fulfils PublisherController`() {
        val controller: Any = PublisherStandaloneController

        assertTrue(controller is PublishingContract.PublisherController)
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no PackageConfiguration was given`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(PackageConfiguration::class.java, null),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        every { project.name } returns fixture()

        // When
        PublisherStandaloneController.configure(
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
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it does nothing if no registryConfiguration was given`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                emptySet(),
            ),
            packaging = makeProperty(PackageConfiguration::class.java, mockk()),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        every { project.name } returns fixture()

        // When
        PublisherStandaloneController.configure(
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
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val tasks: TaskContainer = mockk()
        val task: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { tasks.findByName(any()) } returns task

        every { tasks.create(any(), any<Action<Task>>()) } returns task

        every {
            MavenPublisher.configure(project, packageConfiguration, any(), version)
        } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns task
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns task

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) { MavenPublisher.configure(project, packageConfiguration, any(), version) }

        verify(exactly = 1) {
            MavenRepository.configure(
                project,
                registry1,
                dryRun,
            )
        }

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
            MavenRepository.configure(
                project,
                registry2,
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
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it distributes the configurations for additional dependencies`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val additionalSources: Map<String, Set<String>> = mapOf(
            "x${fixture<String>()}" to setOf(fixture(), fixture()),
        )

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                additionalSources,
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val tasks: TaskContainer = mockk()
        val task: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks
        every { tasks.findByName(any()) } returns task

        every { tasks.create(any(), any<Action<Task>>()) } returns task

        every {
            MavenPublisher.configure(project, packageConfiguration, any(), version)
        } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns task
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns task

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) { MavenPublisher.configure(project, packageConfiguration, any(), version) }

        verify(exactly = 1) {
            MavenRepository.configure(
                project,
                registry1,
                dryRun,
            )
        }

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
                publishingId = additionalSources.keys.first(),
            )
        }

        verify(exactly = 1) {
            MavenRepository.configure(
                project,
                registry2,
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
                publishingId = additionalSources.keys.first(),
            )
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it adds a publishing Task`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val tasks: TaskContainer = mockk()
        val publishingTask: Task = mockk(relaxed = true)
        val task: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()
        every { tasks.findByName(any()) } returns task

        every { MavenPublisher.configure(project, packageConfiguration, any(), version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns task
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns task

        invokeGradleAction(
            publishingTask,
            publishingTask,
        ) { probe -> tasks.create(any(), probe) }
        invokeGradleAction(
            publishingTask,
            publishingTask,
        ) { probe -> tasks.create(any(), probe) }

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) { tasks.create("publish${registry1.name.capitalize()}", any<Action<Task>>()) }
        verify(exactly = 1) { tasks.create("publish${registry2.name.capitalize()}", any<Action<Task>>()) }

        verify(exactly = 2) { publishingTask.group = "Publishing" }
        verify(exactly = 1) { publishingTask.description = "Publish ${registry1.name.capitalize()}" }
        verify(exactly = 1) { publishingTask.description = "Publish ${registry2.name.capitalize()}" }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it adds a publishing Task for additional dependencies`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val additionalSources: Map<String, Set<String>> = mapOf(
            "x${fixture<String>()}" to setOf(fixture(), fixture()),
        )

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                additionalSources,
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val tasks: TaskContainer = mockk()
        val publishingTask: Task = mockk(relaxed = true)
        val task: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns mockk()
        every { tasks.findByName(any()) } returns task

        every { MavenPublisher.configure(project, packageConfiguration, any(), version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns task
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns task

        invokeGradleAction(
            publishingTask,
            publishingTask,
        ) { probe -> tasks.create(any(), probe) }
        invokeGradleAction(
            publishingTask,
            publishingTask,
        ) { probe -> tasks.create(any(), probe) }

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            null,
            config,
        )

        // Then
        verify(exactly = 1) {
            tasks.create(
                "publish${additionalSources.keys.first().capitalize()}${registry1.name.capitalize()}",
                any<Action<Task>>(),
            )
        }
        verify(exactly = 1) {
            tasks.create(
                "publish${additionalSources.keys.first().capitalize()}${registry2.name.capitalize()}",
                any<Action<Task>>(),
            )
        }

        verify(exactly = 4) { publishingTask.group = "Publishing" }
        verify(exactly = 1) {
            publishingTask.description = "Publish ${additionalSources.keys.first().capitalize()} to ${registry1.name.capitalize()}"
        }
        verify(exactly = 1) {
            publishingTask.description = "Publish ${additionalSources.keys.first().capitalize()} to ${registry2.name.capitalize()}"
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val tasks: TaskContainer = mockk()

        val gitCloneTask: Task = mockk(relaxed = true)
        val maven1Task: Task = mockk(relaxed = true)
        val maven2Task: Task = mockk(relaxed = true)
        val gitPushTask: Task = mockk(relaxed = true)
        val publishingTask: Task = mockk(relaxed = true)
        val documentation: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask
        every {
            tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every { MavenPublisher.configure(project, packageConfiguration, documentation, version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns gitCloneTask
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns gitPushTask

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verify(exactly = 1) { tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { maven1Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { maven1Task.mustRunAfter(gitCloneTask) }

        verify(exactly = 1) { gitPushTask.mustRunAfter(listOf(maven1Task)) }
        verify(exactly = 1) { gitPushTask.dependsOn(listOf(maven1Task)) }

        verify(exactly = 1) { maven2Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { gitPushTask.mustRunAfter(listOf(maven2Task)) }
        verify(exactly = 1) { gitPushTask.dependsOn(listOf(maven2Task)) }

        verify(exactly = 2) { publishingTask.mustRunAfter(listOf(gitPushTask)) }
        verify(exactly = 2) { publishingTask.dependsOn(listOf(gitPushTask)) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies for additional tasks`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val additionalSources: Map<String, Set<String>> = mapOf(
            "x${fixture<String>()}" to setOf(fixture(), fixture()),
        )

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                additionalSources,
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val tasks: TaskContainer = mockk()

        val gitCloneTask: Task = mockk(relaxed = true)
        val maven1Task: Task = mockk(relaxed = true)
        val maven2Task: Task = mockk(relaxed = true)
        val gitPushTask: Task = mockk(relaxed = true)
        val publishingTask: Task = mockk(relaxed = true)
        val documentation: Task = mockk(relaxed = true)

        val publishAllMavenTask: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every { tasks.findByName(any()) } returns publishAllMavenTask

        every {
            tasks.findByName(
                "publish${additionalSources.values.first().first().capitalize()}PublicationTo${registry1.name.capitalize()}Repository",
            )
        } returns maven1Task
        every {
            tasks.findByName(
                "publish${additionalSources.values.first().toList()[1].capitalize()}PublicationTo${registry2.name.capitalize()}Repository",
            )
        } returns maven2Task

        every { MavenPublisher.configure(project, packageConfiguration, documentation, version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns gitCloneTask
        every { GitRepository.configurePushTask(project, any(), version, dryRun, any()) } returns gitPushTask

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verify(exactly = 1) {
            tasks.findByName(
                "publish${additionalSources.values.first().first().capitalize()}PublicationTo${registry1.name.capitalize()}Repository",
            )
        }
        verify(exactly = 1) {
            tasks.findByName(
                "publish${additionalSources.values.first().toList()[1].capitalize()}PublicationTo${registry2.name.capitalize()}Repository",
            )
        }

        verify(exactly = 1) { maven1Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { maven1Task.mustRunAfter(gitCloneTask) }
        verify(exactly = 1) { maven2Task.dependsOn(gitCloneTask) }
        verify(exactly = 1) { maven2Task.mustRunAfter(gitCloneTask) }

        verify(exactly = 1) {
            gitPushTask.dependsOn(
                listOf(maven1Task, publishAllMavenTask),
            )
        }
        verify(exactly = 1) {
            gitPushTask.mustRunAfter(
                listOf(publishAllMavenTask, maven2Task),
            )
        }

        verify(exactly = 4) { publishingTask.dependsOn(listOf(gitPushTask)) }
        verify(exactly = 4) { publishingTask.mustRunAfter(listOf(gitPushTask)) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it runs the tasks in order`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()
        val documentation: Task = mockk()

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val tasks: TaskContainer = mockk()

        val gitCloneTask: Task = mockk(relaxed = true)
        val maven1Task: Task = mockk(relaxed = true)
        val maven2Task: Task = mockk(relaxed = true)
        val gitPushTask: Task = mockk(relaxed = true)
        val publishingTask: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask
        every {
            tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every { MavenPublisher.configure(project, packageConfiguration, documentation, version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns gitCloneTask
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns gitPushTask

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verifyOrder {
            MavenPublisher.configure(
                project,
                packageConfiguration,
                documentation,
                version,
            )

            MavenRepository.configure(project, registry1, dryRun)

            GitRepository.configureCloneTask(project, registry1)
            GitRepository.configurePushTask(project, registry1, version, dryRun)

            tasks.create("publish${registry1.name.capitalize()}", any<Action<Task>>())
            tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")

            maven1Task.dependsOn(gitCloneTask)
            maven1Task.mustRunAfter(gitCloneTask)

            gitPushTask.dependsOn(listOf(maven1Task))
            gitPushTask.mustRunAfter(listOf(maven1Task))

            publishingTask.dependsOn(listOf(gitPushTask))
            publishingTask.mustRunAfter(listOf(gitPushTask))

            MavenRepository.configure(project, registry2, dryRun)

            GitRepository.configureCloneTask(project, registry2)
            GitRepository.configurePushTask(project, registry2, version, dryRun)

            tasks.create("publish${registry2.name.capitalize()}", any<Action<Task>>())
            tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")

            maven2Task.dependsOn(gitCloneTask)
            maven2Task.mustRunAfter(gitCloneTask)

            gitPushTask.dependsOn(listOf(maven2Task))
            gitPushTask.mustRunAfter(listOf(maven2Task))

            publishingTask.dependsOn(listOf(gitPushTask))
            publishingTask.mustRunAfter(listOf(gitPushTask))
        }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies while Git is not in use`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )
        val documentation: Task = mockk()

        val tasks: TaskContainer = mockk()

        val maven1Task: Task = mockk(relaxed = true)
        val maven2Task: Task = mockk(relaxed = true)
        val publishingTask: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask
        every {
            tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every { MavenPublisher.configure(project, packageConfiguration, documentation, version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns null
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns null

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verify(exactly = 1) { tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository") }
        verify(exactly = 1) { tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository") }

        verify(exactly = 1) { publishingTask.dependsOn(listOf(maven1Task)) }
        verify(exactly = 1) { publishingTask.mustRunAfter(listOf(maven1Task)) }
        verify(exactly = 1) { publishingTask.dependsOn(listOf(maven2Task)) }
        verify(exactly = 1) { publishingTask.mustRunAfter(listOf(maven2Task)) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it wires the dependencies while Git is not in use for additional dependencies`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val additionalSources: Map<String, Set<String>> = mapOf(
            "x${fixture<String>()}" to setOf(fixture(), fixture()),
        )

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                additionalSources,
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )
        val documentation: Task = mockk()

        val tasks: TaskContainer = mockk()

        val maven1Task: Task = mockk(relaxed = true)
        val maven2Task: Task = mockk(relaxed = true)
        val publishingTask: Task = mockk(relaxed = true)

        val publishAllMavenTask: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask

        every { tasks.findByName(any()) } returns publishAllMavenTask

        every {
            tasks.findByName(
                "publish${additionalSources.values.first().first().capitalize()}PublicationTo${registry1.name.capitalize()}Repository",
            )
        } returns maven1Task
        every {
            tasks.findByName(
                "publish${additionalSources.values.first().toList()[1].capitalize()}PublicationTo${registry2.name.capitalize()}Repository",
            )
        } returns maven2Task

        every { MavenPublisher.configure(project, packageConfiguration, documentation, version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns null
        every { GitRepository.configurePushTask(project, any(), version, dryRun, any()) } returns null

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verify(exactly = 1) {
            tasks.findByName(
                "publish${additionalSources.values.first().first().capitalize()}PublicationTo${registry1.name.capitalize()}Repository",
            )
        }
        verify(exactly = 1) {
            tasks.findByName(
                "publish${additionalSources.values.first().toList()[1].capitalize()}PublicationTo${registry2.name.capitalize()}Repository",
            )
        }

        verify(exactly = 1) { publishingTask.dependsOn(listOf(maven1Task, publishAllMavenTask)) }
        verify(exactly = 1) { publishingTask.mustRunAfter(listOf(maven1Task, publishAllMavenTask)) }
        verify(exactly = 1) { publishingTask.dependsOn(listOf(publishAllMavenTask, maven2Task)) }
        verify(exactly = 1) { publishingTask.mustRunAfter(listOf(publishAllMavenTask, maven2Task)) }
    }

    @Test
    fun `Given configure is called with a Project and PublishingPluginConfiguration, it runs the tasks in order while Git is not in use`() {
        // Given
        val project: Project = mockk {
            every { project } returns this
        }
        val registry1 = gitRegistryTestConfig.copy(name = "a")
        val registry2 = mavenRegistryTestConfig.copy(name = "b")
        val dryRun: Boolean = fixture()
        val version: String = fixture()

        val repositoriesConfiguration: Set<RepositoryConfiguration<out Any>> = setOf(registry1, registry2)
        val packageConfiguration: PackageConfiguration = mockk()
        val versioningConfiguration: VersioningConfiguration = mockk()

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = makeSetProperty(
                RepositoryConfiguration::class.java,
                repositoriesConfiguration,
            ),
            packaging = makeProperty(PackageConfiguration::class.java, packageConfiguration),
            dryRun = makeProperty(Boolean::class.java, dryRun),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningConfiguration::class.java,
                versioningConfiguration,
            ),
            standalone = makeProperty(Boolean::class.java, true),
        )
        val documentation: Task = mockk()

        val tasks: TaskContainer = mockk()

        val maven1Task: Task = mockk(relaxed = true)
        val maven2Task: Task = mockk(relaxed = true)
        val publishingTask: Task = mockk(relaxed = true)

        every { project.name } returns fixture()
        every { project.tasks } returns tasks

        every { tasks.create(any(), any<Action<Task>>()) } returns publishingTask
        every {
            tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")
        } returns maven1Task
        every {
            tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")
        } returns maven2Task

        every { MavenPublisher.configure(project, packageConfiguration, documentation, version) } just Runs
        every { MavenRepository.configure(project, any(), dryRun) } just Runs
        every { GitRepository.configureCloneTask(project, any()) } returns null
        every { GitRepository.configurePushTask(project, any(), version, dryRun) } returns null

        // When
        PublisherStandaloneController.configure(
            project,
            version,
            documentation,
            config,
        )

        // Then
        verifyOrder {
            MavenPublisher.configure(
                project,
                packageConfiguration,
                documentation,
                version,
            )

            MavenRepository.configure(project, registry1, dryRun)

            GitRepository.configureCloneTask(project, registry1)
            GitRepository.configurePushTask(project, registry1, version, dryRun)

            tasks.create("publish${registry1.name.capitalize()}", any<Action<Task>>())
            tasks.findByName("publishAllPublicationsTo${registry1.name.capitalize()}Repository")

            publishingTask.dependsOn(listOf(maven1Task))
            publishingTask.mustRunAfter(listOf(maven1Task))

            MavenRepository.configure(project, registry2, dryRun)

            GitRepository.configureCloneTask(project, registry2)
            GitRepository.configurePushTask(project, registry2, version, dryRun)

            tasks.create("publish${registry2.name.capitalize()}", any<Action<Task>>())
            tasks.findByName("publishAllPublicationsTo${registry2.name.capitalize()}Repository")

            publishingTask.dependsOn(listOf(maven2Task))
            publishingTask.mustRunAfter(listOf(maven2Task))
        }
    }
}
