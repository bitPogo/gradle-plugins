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
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.coverage.configuration.PlatformContextResolver
import tech.antibytes.gradle.coverage.task.TaskController
import tech.antibytes.gradle.publishing.invokeGradleAction
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AntiBytesCoverageSpec {
    @Before
    fun setup() {
        mockkObject(TaskController)
        mockkObject(PlatformContextResolver)

        every { PlatformContextResolver.isKmp(any<Project>()) } returns false
    }

    @After
    fun tearDown() {
        unmockkObject(TaskController)
        unmockkObject(PlatformContextResolver)
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesCoverage()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and processes it after the project was evaluated`() {
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java, any())
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
        verify(exactly = 1) {
            project.extensions.create(
                "antiBytesCoverage",
                AntiBytesCoverageExtension::class.java,
                project
            )
        }
        verify(exactly = 1) { plugins.apply("jacoco") }
        verify(exactly = 1) { project.evaluationDependsOnChildren() }
        verify(exactly = 1) { TaskController.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not override an existing contexts`() {
        mockkObject(DefaultConfigurationProvider)

        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context" to mockk()
        )
        val defaultConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf(
            "context" to mockk()
        )

        val actualConfigurations = givenConfigurations.toMutableMap()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java, any())
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

        every { PlatformContextResolver.isKmp(project) } returns true
        every { extension.appendKmpJvmTask } returns true
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations,
            expected = givenConfigurations
        )

        unmockkObject(DefaultConfigurationProvider)
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not override an existing jvm contexts`() {
        mockkObject(DefaultConfigurationProvider)

        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
        )
        val defaultConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk()
        )

        val actualConfigurations = givenConfigurations.toMutableMap()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java, any())
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

        every { PlatformContextResolver.isKmp(project) } returns true
        every { extension.appendKmpJvmTask } returns true
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations,
            expected = givenConfigurations
        )

        unmockkObject(DefaultConfigurationProvider)
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and adds missing the jvm contexts, if the project is KMP and has no preset jvm`() {
        mockkObject(DefaultConfigurationProvider)

        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf()
        val defaultConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk()
        )

        val actualConfigurations = givenConfigurations.toMutableMap()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java, any())
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

        every { PlatformContextResolver.isKmp(project) } returns true
        every { extension.appendKmpJvmTask } returns true
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations,
            expected = givenConfigurations.also {
                it["jvm"] = defaultConfigurations["jvm"]!!
            }
        )

        unmockkObject(DefaultConfigurationProvider)
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not add a jvm contexts, if the append policy is false`() {
        mockkObject(DefaultConfigurationProvider)

        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf()
        val defaultConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk()
        )

        val actualConfigurations = givenConfigurations.toMutableMap()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java, any())
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

        every { PlatformContextResolver.isKmp(project) } returns true
        every { extension.appendKmpJvmTask } returns false
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations,
            expected = givenConfigurations
        )

        unmockkObject(DefaultConfigurationProvider)
    }

    @Test
    fun `Given apply is called with a Project, it will not applies the JacocoPlugin, if it is already present`() {
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoverageExtension = mockk()
        val plugins: PluginContainer = mockk()

        every {
            project.extensions.create("antiBytesCoverage", AntiBytesCoverageExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.findPlugin("jacoco") } returns mockk()
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mutableMapOf()

        every { project.afterEvaluate(any<Action<Project>>()) } just Runs

        every { TaskController.configure(any(), any()) } just Runs

        // When
        AntiBytesCoverage().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("jacoco") }
    }
}