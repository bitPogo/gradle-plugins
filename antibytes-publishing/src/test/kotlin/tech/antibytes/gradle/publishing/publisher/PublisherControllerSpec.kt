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
import org.gradle.jvm.tasks.Jar
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.api.DocumentationConfiguration
import tech.antibytes.gradle.publishing.doc.DocumentationController
import tech.antibytes.gradle.publishing.version.VersionController
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeSetProperty
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.versioning.VersioningContract.VersioningConfiguration

class PublisherControllerSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkObject(DocumentationController)
        mockkObject(VersionController)
        mockkObject(PublisherRootProjectController)
        mockkObject(PublisherSubProjectController)
        mockkObject(PublisherStandaloneController)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(DocumentationController)
        unmockkObject(VersionController)
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
    fun `Given configure is called with a Project and the Configuration the Project if it is excludes`() {
        // Given
        val name: String = fixture()
        val version: String = fixture()
        val documentationTask: Jar = mockk()
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

        every { project.name } returns name
        every { project.rootProject } returns root

        every { VersionController.configure(any(), any()) } returns version
        every { DocumentationController.configure(any(), any()) } returns documentationTask

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
        verify(exactly = 1) { VersionController.configure(project, config) }
        verify(exactly = 1) { DocumentationController.configure(project, config) }

        verify(exactly = 0) { PublisherStandaloneController.configure(any(), version, any(), any()) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), version, any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), version, any(), any()) }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the Standalone Configuration, if it is configured as Standalone`() {
        // Given
        val version: String = fixture()
        val documentationTask: Jar? = null
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

        every { project.name } returns fixture()
        every { project.rootProject } returns root

        every { VersionController.configure(any(), any()) } returns version
        every { DocumentationController.configure(any(), any()) } returns documentationTask

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
        verify(exactly = 1) { VersionController.configure(project, config) }
        verify(exactly = 1) { DocumentationController.configure(project, config) }

        verify(exactly = 1) { PublisherStandaloneController.configure(project, version, null, config) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the SubProjectPublisher, if it is configured as non Standalone and the target is not root`() {
        // Given
        val version: String = fixture()
        val documentationTask: Jar = mockk()
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

        every { project.name } returns fixture()
        every { project.rootProject } returns root

        every { VersionController.configure(any(), any()) } returns version
        every { DocumentationController.configure(any(), any()) } returns documentationTask

        every { PublisherStandaloneController.configure(any(), any(), any(), any()) } just Runs
        every { PublisherRootProjectController.configure(any(), any(), any(), any()) } just Runs
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
        verify(exactly = 1) { VersionController.configure(project, config) }
        verify(exactly = 1) { DocumentationController.configure(project, config) }

        verify(exactly = 0) { PublisherStandaloneController.configure(any(), any(), any(), any()) }
        verify(exactly = 1) { PublisherSubProjectController.configure(project, version, documentationTask, config) }
        verify(exactly = 0) { PublisherRootProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it sets up the evaluation dependencies if the target is root`() {
        // Given
        val version: String = fixture()
        val documentationTask: Jar = mockk()

        val config = TestConfig(
            repositories = mockk(),
            packaging = mockk(),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf()),
            versioning = makeProperty(VersioningConfiguration::class.java, mockk(relaxed = true)),
            standalone = makeProperty(Boolean::class.java, false),
        )

        val project: Project = mockk()

        every { project.name } returns fixture()
        every { project.rootProject } returns project
        every { project.evaluationDependsOnChildren() } just Runs

        every { VersionController.configure(any(), any()) } returns version
        every { DocumentationController.configure(any(), any()) } returns documentationTask

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
        verify(exactly = 1) { VersionController.configure(project, config) }
        verify(exactly = 1) { DocumentationController.configure(project, config) }

        verify(exactly = 1) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called with a Project and the Configuration, it delegates the parameter to the RootProjectPublisher if the target is root and it is configured as non Standalone `() {
        // Given
        val version: String = fixture()
        val documentationTask: Jar = mockk()
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

        every { project.name } returns fixture()
        every { project.rootProject } returns project
        every { project.evaluationDependsOnChildren() } just Runs

        every { VersionController.configure(any(), any()) } returns version
        every { DocumentationController.configure(any(), any()) } returns documentationTask

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
        verify(exactly = 1) { VersionController.configure(project, config) }
        verify(exactly = 1) { DocumentationController.configure(project, config) }

        verify(exactly = 0) { PublisherStandaloneController.configure(any(), any(), any(), any()) }
        verify(exactly = 0) { PublisherSubProjectController.configure(any(), any(), any(), any()) }
        verify(exactly = 1) { PublisherRootProjectController.configure(project, version, documentationTask, config) }
    }
}
