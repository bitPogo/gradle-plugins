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
import org.gradle.api.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.api.MemorySigningConfiguration
import tech.antibytes.gradle.publishing.publisher.TestConfig
import tech.antibytes.gradle.test.invokeGradleAction
import kotlin.test.assertTrue

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
            mockk()
        )

        // When
        SigningController.configure(project, config)

        // Then
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
                fixture(), fixture()
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
            mockk()
        )

        // When
        SigningController.configure(project, config)

        // Then
        verify(exactly = 1) { MemorySigning.configure(any(), any()) }
    }
}
