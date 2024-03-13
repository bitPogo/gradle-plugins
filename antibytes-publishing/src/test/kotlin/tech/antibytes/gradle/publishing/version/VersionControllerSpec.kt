/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.version

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.TaskContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.publishing.publisher.TestConfig
import tech.antibytes.gradle.test.GradlePropertyFactory.makeMapProperty
import tech.antibytes.gradle.test.GradlePropertyFactory.makeProperty
import tech.antibytes.gradle.test.GradlePropertyFactory.makeSetProperty
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.VersioningContract
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration
import tech.antibytes.gradle.versioning.api.VersionInfo

@Suppress("UNCHECKED_CAST")
class VersionControllerSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkObject(Versioning)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(Versioning)
    }

    @Test
    fun `It fulfils VersionController`() {
        val controller: Any = VersionController

        assertTrue(controller is PublisherContract.VersionController)
    }

    @Test
    fun `Given configure is called with a Project, Configuration it adds a version to the Project`() {
        // Given
        val name: String = fixture()
        val version: String = fixture()
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val versioning: VersioningContract.Versioning = mockk()

        every { project.tasks } returns tasks

        every { tasks.findByName("versionInfo") } returns mockk()

        every { Versioning.getInstance(project, versionConfig) } returns versioning
        every { project.version = any<String>() } just Runs
        every { versioning.versionName() } returns version

        // When
        VersionController.configure(project = project, configuration = config)

        // Then
        verify(exactly = 1) { project.version = version }
    }

    @Test
    fun `Given configure is called with a Project Configuration it adds a Versioning task to the Project if it does not exists`() {
        // Given
        val name: String = fixture()
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val versioningTask: Task = mockk()
        val versioning: VersioningContract.Versioning = mockk()

        every { project.tasks } returns tasks

        every { tasks.findByName(any()) } returns mockk()
        every { tasks.getByName(any()) } returns mockk()
        every { tasks.findByName("versionInfo") } returns null

        every { versioningTask.project } returns project
        every { versioningTask.group = any() } just Runs
        every { versioningTask.description = any() } just Runs
        every { Versioning.getInstance(project, versionConfig) } returns versioning
        every { versioning.versionInfo() } returns VersionInfo(
            fixture(),
            mockk(relaxed = true),
        )
        every { project.version = any<String>() } just Runs
        every { versioning.versionName() } returns fixture()

        invokeGradleAction(
            versioningTask,
            mockk(),
        ) { probe ->
            tasks.create("versionInfo", probe)
        }

        invokeGradleAction(
            versioningTask,
            mockk(),
        ) { probe ->
            versioningTask.doLast(probe)
        }

        // When
        VersionController.configure(project = project, configuration = config)

        // Then
        verify(exactly = 1) { versioningTask.group = "Versioning" }
        verify(exactly = 1) { versioningTask.description = "Displays the current version" }
        verify(exactly = 1) { versioning.versionInfo() }
    }

    @Test
    fun `Given configure is called with a Project Configuration it does nothing if the VersionTask already exists`() {
        // Given
        val name: String = fixture()
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)

        val config = TestConfig(
            additionalPublishingTasks = makeMapProperty(
                String::class.java,
                Set::class.java,
                emptyMap(),
            ) as MapProperty<String, Set<String>>,
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val versioningTask: Task = mockk()
        val versioning: VersioningContract.Versioning = mockk()

        every { project.tasks } returns tasks

        every { tasks.findByName(any()) } returns mockk()
        every { tasks.getByName(any()) } returns mockk()
        every { tasks.findByName("versionInfo") } returns mockk()

        every { versioningTask.project } returns project
        every { versioningTask.group = any() } just Runs
        every { versioningTask.description = any() } just Runs
        every { Versioning.getInstance(project, versionConfig) } returns versioning
        every { versioning.versionInfo() } returns VersionInfo(
            fixture(),
            mockk(relaxed = true),
        )
        every { project.version = any<String>() } just Runs
        every { versioning.versionName() } returns fixture()

        invokeGradleAction(
            versioningTask,
            mockk(),
        ) { probe ->
            tasks.create("versionInfo", probe)
        }

        invokeGradleAction(
            versioningTask,
            mockk(),
        ) { probe ->
            versioningTask.doLast(probe)
        }

        // When
        VersionController.configure(project = project, configuration = config)

        // Then
        verify(exactly = 0) { versioningTask.group = "Versioning" }
        verify(exactly = 0) { versioningTask.description = "Displays the current version" }
        verify(exactly = 0) { versioning.versionInfo() }
    }
}
