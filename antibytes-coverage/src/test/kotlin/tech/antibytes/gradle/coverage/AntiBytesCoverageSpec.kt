/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.Test
import tech.antibytes.gradle.coverage.task.TaskController
import tech.antibytes.gradle.publishing.invokeGradleAction
import kotlin.test.assertTrue

class AntiBytesCoverageSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesCoverage()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and processes it after the project was evaluated`() {
        mockkObject(TaskController)
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java)
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.findPlugin("jacoco") } returns null
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project
        )

        every { TaskController.configure(any(), any()) } just Runs

        // When
        AntiBytesCoverage().apply(project)

        // Then
        verify(exactly = 1) { plugins.apply("jacoco") }
        verify(exactly = 1) { project.evaluationDependsOnChildren() }
        verify(exactly = 1) { TaskController.configure(project, extension) }

        unmockkObject(TaskController)
    }

    @Test
    fun `Given apply is called with a Project, it will not applies the JacocoPlugin, if it is already present`() {
        mockkObject(TaskController)
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java)
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.findPlugin("jacoco") } returns mockk()
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project
        )

        every { TaskController.configure(any(), any()) } just Runs

        // When
        AntiBytesCoverage().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("jacoco") }

        unmockkObject(TaskController)
    }
}
