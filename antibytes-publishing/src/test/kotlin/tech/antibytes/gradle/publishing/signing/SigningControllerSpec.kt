/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingApiContract.MemorySigning
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.api.MemorySigningConfiguration
import tech.antibytes.gradle.publishing.publisher.TestConfig
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeSetProperty
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.versioning.VersioningContract

class SigningControllerSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkObject(
            MemorySignature,
            CommonSignature,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            MemorySignature,
            CommonSignature,
        )
    }

    @Test
    fun `It fulfils SigningController`() {
        val controller: Any = SigningController

        assertTrue(controller is PublishingContract.SigningController)
    }

    @Test
    fun `Given configure is called with a project which is the root project it triggers evaluationDependsOnChildren`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(MemorySigning::class.java, null),
        )

        val project: Project = mockk(relaxed = true)

        every { project.name } returns name
        every { project.rootProject } returns project

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { project.evaluationDependsOnChildren() }
    }

    @Test
    fun `Given configure is called without signing configuration, it does not call MemorySigning`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(MemorySigning::class.java, null),
        )

        val project: Project = mockk()
        val root: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(any()) }
        verify(exactly = 0) { MemorySignature.configure(any(), any()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration, it calls MemorySigning`() {
        // Given
        val name: String = fixture()
        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val root: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { CommonSignature.configure(project) }
        verify(exactly = 1) { MemorySignature.configure(project, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it is excluded by the root`() {
        // Given
        val name: String = fixture()
        val subName: String = fixture()
        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, setOf(subName)),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns subName

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if they have no PublishingExtension`() {
        // Given
        val name: String = fixture()
        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns name

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns null

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it is a standalone`() {
        // Given
        val name: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns name

        subprojectExtension.standalone.set(true)

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it has a signing Configuration`() {
        // Given
        val name: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns name

        subprojectExtension.standalone.set(false)
        subprojectExtension.signing.set(mockk<MemorySigning>())

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it ignores subprojects if it is excluded`() {
        // Given
        val name: String = fixture()
        val subName: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns subName

        subprojectExtension.standalone.set(false)
        subprojectExtension.signing.set(null)
        subprojectExtension.excludeProjects.set(setOf(subName))

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSignature.configure(subProject) }
        verify(exactly = 0) { MemorySignature.configure(subProject, config.signing.get()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it configures the subprojects`() {
        // Given
        val name: String = fixture()
        val subName: String = fixture()
        val subprojectExtension: PublishingPluginExtension = createExtension()
        val config = TestConfig(
            repositories = makeSetProperty(
                PublishingApiContract.RepositoryConfiguration::class.java,
                setOf(mockk()),
            ),
            packaging = makeProperty(
                PublishingApiContract.PackageConfiguration::class.java,
                mockk(),
            ),
            dryRun = makeProperty(Boolean::class.java, false),
            excludeProjects = makeSetProperty(String::class.java, emptySet()),
            versioning = makeProperty(
                VersioningContract.VersioningConfiguration::class.java,
                mockk(),
            ),
            standalone = makeProperty(Boolean::class.java, true),
            signing = makeProperty(
                MemorySigning::class.java,
                MemorySigningConfiguration(
                    fixture(),
                    fixture(),
                ),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)
        every { subProject.name } returns subName

        subprojectExtension.standalone.set(false)
        subprojectExtension.signing.set(null)
        subprojectExtension.excludeProjects.set(emptySet())

        every {
            subProject.extensions.findByType(PublishingPluginExtension::class.java)
        } returns subprojectExtension

        every { MemorySignature.configure(any(), any()) } just Runs
        every { CommonSignature.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { CommonSignature.configure(subProject) }
        verify(exactly = 1) { MemorySignature.configure(subProject, config.signing.get()) }
    }
}
