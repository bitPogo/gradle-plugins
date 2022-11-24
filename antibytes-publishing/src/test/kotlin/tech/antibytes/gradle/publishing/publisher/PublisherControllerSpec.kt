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
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.tasks.Jar
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.api.DocumentationConfiguration
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeSetProperty
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.VersioningContract
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration
import tech.antibytes.gradle.versioning.api.VersionInfo

class PublisherControllerSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkObject(Versioning)
        mockkObject(PublisherRootProjectController)
        mockkObject(PublisherSubProjectController)
        mockkObject(PublisherStandaloneController)
    }

    @AfterEach
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
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)

        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()
        val versioningTask: Task = mockk()
        val versioning: VersioningContract.Versioning = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
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

        every {
            PublisherStandaloneController.configure(any(), any(), any(), any())
        } just Runs
        every {
            PublisherRootProjectController.configure(any(), any(), any(), any())
        } just Runs
        every {
            PublisherSubProjectController.configure(any(), any(), any(), any())
        } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        invokeGradleAction(
            { probe -> tasks.create("versionInfo", probe) },
            versioningTask,
            mockk(),
        )

        invokeGradleAction(
            { probe -> versioningTask.doLast(probe) },
            versioningTask,
            mockk(),
        )

        // When
        PublisherController.configure(project = project, extension = config)

        // Then
        verify(exactly = 1) { versioningTask.group = "Versioning" }
        verify(exactly = 1) { versioningTask.description = "Displays the current version" }
        verify(exactly = 1) { versioning.versionInfo() }
    }

    @Test
    fun `Given configure is called with a Project, Configuration and a IsRoot Flag, it will not a Versioning task to the Project root if the task already exists`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, mockk(relaxed = true)),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()
        val versioningTask: Task = mockk()
        val versioning: VersioningContract.Versioning = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName("versionInfo") } returns mockk()

        every { versioningTask.group = any() } just Runs
        every { versioningTask.description = any() } just Runs
        every { project.version = any<String>() } just Runs
        every {
            Versioning.getInstance(
                any(),
                any(),
            ).versionName()
        } returns fixture()

        every { PublisherStandaloneController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any(), any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        invokeGradleAction(
            { probe -> tasks.create("versionInfo", probe) },
            versioningTask,
            mockk(),
        )

        invokeGradleAction(
            { probe -> versioningTask.doLast(probe) },
            versioningTask,
            mockk(),
        )

        // When
        PublisherController.configure(project = project, extension = config)

        // Then
        verify(exactly = 1) {
            Versioning.getInstance(project, config.versioning.get())
        }
        verify(exactly = 0) { versioningTask.group = "Versioning" }
        verify(exactly = 0) { versioningTask.description = "Displays the current version" }
        verify(exactly = 0) { versioning.versionInfo() }
    }

    @Test
    fun `Given configure is called with a Project, Configuration and a IsRoot Flag, it adds a Documentation task to the Project root if it does not exists`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, mockk(relaxed = true)),
            standalone = makeProperty(Boolean::class.java, true),
            documentation = makeProperty(
                PublishingApiContract.DocumentationConfiguration::class.java,
                DocumentationConfiguration(
                    tasks = setOf(fixture()),
                    outputDir = fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val rootTasks: TaskContainer = mockk()
        val childTasks: TaskContainer = mockk()
        val javaDocTask: Jar = mockk()

        every { project.name } returns name
        every { project.tasks } returns childTasks
        every { project.rootProject } returns root
        every { root.tasks } returns rootTasks
        every { rootTasks.findByName(any()) } returns mockk()
        every { childTasks.findByName("javadoc") } returns null

        every { javaDocTask.group = any() } just Runs
        every { javaDocTask.description = any() } just Runs
        every { javaDocTask.setDependsOn(any()) } just Runs
        every { javaDocTask.archiveClassifier.set(any<String>()) } just Runs
        every { javaDocTask.from(any()) } returns mockk()

        every { Versioning.getInstance(any(), any()) } returns mockk(relaxed = true)
        every {
            PublisherStandaloneController.configure(any(), any(), any(), any())
        } just Runs
        every {
            PublisherRootProjectController.configure(any(), any(), any(), any())
        } just Runs
        every {
            PublisherSubProjectController.configure(any(), any(), any(), any())
        } just Runs
        every { project.version = any() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        invokeGradleAction(
            { probe -> childTasks.create("javadoc", Jar::class.java, probe) },
            javaDocTask,
            mockk(),
        )

        // When
        PublisherController.configure(project = project, extension = config)

        // Then
        verify(exactly = 1) { javaDocTask.group = "Documentation" }
        verify(exactly = 1) { javaDocTask.description = "Generates the JavaDocs" }
        verify(exactly = 1) { javaDocTask.setDependsOn(config.documentation.get().tasks) }
        verify(exactly = 1) { javaDocTask.archiveClassifier.set("javadoc") }
        verify(exactly = 1) { javaDocTask.from(config.documentation.get().outputDir.absolutePath) }
    }

    @Test
    fun `Given configure is called with a Project, Configuration and a IsRoot Flag, it will not add a Documentation task to the Project root if it already exists`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, mockk(relaxed = true)),
            standalone = makeProperty(Boolean::class.java, true),
            documentation = makeProperty(
                PublishingApiContract.DocumentationConfiguration::class.java,
                DocumentationConfiguration(
                    tasks = setOf(fixture()),
                    outputDir = fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val rootTasks: TaskContainer = mockk()
        val childTasks: TaskContainer = mockk()
        val javaDocTask: Jar = mockk(relaxed = true)

        every { project.name } returns name
        every { project.tasks } returns childTasks
        every { project.rootProject } returns root
        every { root.tasks } returns rootTasks
        every { rootTasks.findByName(any()) } returns mockk()
        every { childTasks.findByName("javadoc") } returns mockk()
        every { childTasks.getByName("javadoc") } returns mockk()

        every { Versioning.getInstance(any(), any()) } returns mockk(relaxed = true)
        every {
            PublisherStandaloneController.configure(any(), any(), any(), any())
        } just Runs
        every {
            PublisherRootProjectController.configure(any(), any(), any(), any())
        } just Runs
        every {
            PublisherSubProjectController.configure(any(), any(), any(), any())
        } just Runs
        every { project.version = any() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        invokeGradleAction(
            { probe -> childTasks.create("javadoc", Jar::class.java, probe) },
            javaDocTask,
            mockk(),
        )

        // When
        PublisherController.configure(project = project, extension = config)

        // Then
        verify(exactly = 0) { javaDocTask.group = "Documentation" }
        verify(exactly = 0) { javaDocTask.description = "Generates the JavaDocs" }
        verify(exactly = 0) { javaDocTask.setDependsOn(config.documentation.get().tasks) }
        verify(exactly = 0) { javaDocTask.archiveClassifier.set("javadoc") }
        verify(exactly = 0) { javaDocTask.from(config.documentation.get().outputDir.absolutePath) }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration the Project if it is excludes`() {
        // Given
        val name: String = fixture()
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)

        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(name)),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()
        val version: String = fixture()

        every { project.name } returns name
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()
        every { tasks.getByName("javadoc") } returns mockk()

        every {
            Versioning.getInstance(project, versionConfig).versionName()
        } returns version

        every { project.version = version } just Runs
        every { PublisherStandaloneController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any(), any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        PublisherController.configure(project = project, extension = config)

        // Then
        verify(exactly = 1) { project.version = version }

        verify(exactly = 0) { PublisherStandaloneController.configure(any(), version, any(), any()) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), version, any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), version, any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the Standalone Configuration, if it is configured as Standalone`() {
        // Given
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)
        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf()),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, true),
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val tasks: TaskContainer = mockk()
        val version: String = fixture()
        val documentation: Task = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns root
        every { root.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()
        every { tasks.getByName("javadoc") } returns documentation

        every {
            Versioning.getInstance(project, versionConfig).versionName()
        } returns version
        every { project.version = version } just Runs

        every { PublisherStandaloneController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any(), any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        PublisherController.configure(project = project, extension = config)

        // Then
        verify(exactly = 1) { project.version = version }

        verify(exactly = 1) { PublisherStandaloneController.configure(project, version, null, config) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the SubProjectPublisher, if it is configured as non Standalone and the target is not root`() {
        // Given
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)
        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf()),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, false),
            documentation = makeProperty(
                PublishingApiContract.DocumentationConfiguration::class.java,
                DocumentationConfiguration(
                    tasks = setOf(fixture()),
                    outputDir = fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val root: Project = mockk()
        val rootTasks: TaskContainer = mockk()
        val childTasks: TaskContainer = mockk()
        val version: String = fixture()
        val documentation: Jar = mockk()

        every { project.name } returns fixture()
        every { project.tasks } returns childTasks
        every { project.rootProject } returns root
        every { root.tasks } returns rootTasks
        every { rootTasks.findByName(any()) } returns mockk()
        every { childTasks.findByName("javadoc") } returns null
        every { childTasks.getByName("javadoc") } returns documentation

        every {
            Versioning.getInstance(project, versionConfig).versionName()
        } returns version
        every { project.version = version } just Runs

        every { PublisherStandaloneController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any(), any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        every { childTasks.create("javadoc", Jar::class.java, any()) } returns documentation

        // When
        PublisherController.configure(
            project = project,
            extension = config,
        )

        // Then
        verify(exactly = 1) { project.version = version }

        verify(exactly = 0) { PublisherStandaloneController.configure(any(), any(), any(), any()) }
        verify(exactly = 1) { PublisherSubProjectController.configure(project, version, documentation, config) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it sets up the evaluation dependencies if the target is root`() {
        // Given
        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf()),
            versioning = makeProperty(VersioningConfiguration::class.java, mockk(relaxed = true)),
            standalone = makeProperty(Boolean::class.java, false),
        )

        val project: Project = mockk()
        val tasks: TaskContainer = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns project
        every { project.tasks } returns tasks
        every { tasks.findByName(any()) } returns mockk()
        every { project.evaluationDependsOnChildren() } just Runs

        every { project.version = any<String>() } just Runs
        every {
            Versioning.getInstance(
                any(),
                any(),
            ).versionName()
        } returns fixture()

        every { PublisherStandaloneController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherSubProjectController.configure(any(), any(), any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        PublisherController.configure(project = project, extension = config)

        // Then
        verify(exactly = 1) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the RootProjectPublisher if the target is root and it is configured as non Standalone `() {
        // Given
        val versionConfig: VersioningConfiguration = mockk(relaxed = true)
        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf()),
            versioning = makeProperty(VersioningConfiguration::class.java, versionConfig),
            standalone = makeProperty(Boolean::class.java, false),
            documentation = makeProperty(
                PublishingApiContract.DocumentationConfiguration::class.java,
                DocumentationConfiguration(
                    tasks = setOf(fixture()),
                    outputDir = fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val version: String = fixture()
        val documentation: Jar = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns project
        every { project.tasks } returns tasks
        every { tasks.getByName("javadoc") } returns documentation
        every { tasks.findByName(any()) } returns mockk()
        every { project.evaluationDependsOnChildren() } just Runs

        every {
            Versioning.getInstance(project, versionConfig).versionName()
        } returns version
        every { project.version = version } just Runs

        every { PublisherStandaloneController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherRootProjectController.configure(project, version, any(), config) } just Runs
        every { PublisherSubProjectController.configure(any(), any(), any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        PublisherController.configure(
            project = project,
            extension = config,
        )

        // Then
        verify(exactly = 1) { project.version = version }

        verify(exactly = 0) { PublisherStandaloneController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 1) { PublisherRootProjectController.configure(project, version, documentation, config) }
    }
}
