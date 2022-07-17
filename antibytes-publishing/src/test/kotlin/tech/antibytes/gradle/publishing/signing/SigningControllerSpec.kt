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
import tech.antibytes.gradle.publishing.api.MemorySigningConfiguration
import tech.antibytes.gradle.publishing.publisher.TestConfig
import tech.antibytes.gradle.test.invokeGradleAction

class SigningControllerSpec {
    private val fixture = kotlinFixture()

    @BeforeEach
    fun setUp() {
        mockkObject(MemorySigning)
        mockkObject(CommonSigning)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(MemorySigning)
        unmockkObject(CommonSigning)
    }

    @Test
    fun `It fulfils SigningController`() {
        val controller: Any = SigningController

        assertTrue(controller is SigningContract.SigningController)
    }

    @Test
    fun `Given configure is called with a project which is the root project it triggers evaluationDependsOnChildren`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositoryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
            standalone = true,
            signingConfiguration = null,
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
            repositoryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
            standalone = true,
            signingConfiguration = null,
        )

        val project: Project = mockk()
        val root: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root

        every { MemorySigning.configure(any(), any()) } just Runs
        every { CommonSigning.configure(any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 0) { CommonSigning.configure(any()) }
        verify(exactly = 0) { MemorySigning.configure(any(), any()) }
    }

    @Test
    fun `Given configure is called with valid signing configuration, it calls MemorySigning`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositoryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
            standalone = true,
            signingConfiguration = MemorySigningConfiguration(
                fixture(),
                fixture(),
            ),
        )

        val project: Project = mockk()
        val root: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns root

        every { MemorySigning.configure(any(), any()) } just Runs
        every { CommonSigning.configure(any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { CommonSigning.configure(project) }
        verify(exactly = 1) { MemorySigning.configure(project, config.signingConfiguration!!) }
    }

    @Test
    fun `Given configure is called with valid signing configuration on a root project, it configures the subprojects`() {
        // Given
        val name: String = fixture()

        val config = TestConfig(
            repositoryConfiguration = mockk(),
            packageConfiguration = mockk(),
            dryRun = false,
            excludeProjects = setOf(name),
            versioning = mockk(),
            standalone = true,
            signingConfiguration = MemorySigningConfiguration(
                fixture(),
                fixture(),
            ),
        )

        val project: Project = mockk()
        val subProject: Project = mockk()

        every { project.name } returns name
        every { project.rootProject } returns project
        every { project.subprojects } returns setOf(subProject)

        every { MemorySigning.configure(any(), any()) } just Runs
        every { CommonSigning.configure(any()) } just Runs
        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            mockk(),
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { CommonSigning.configure(subProject) }
        verify(exactly = 1) { MemorySigning.configure(subProject, config.signingConfiguration!!) }
    }
}
