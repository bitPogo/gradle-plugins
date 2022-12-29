/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
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
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.coverage.task.TaskController
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeMapProperty
import tech.antibytes.gradle.test.GradlePropertyBuilder.makeProperty
import tech.antibytes.gradle.test.invokeGradleAction

class AntiBytesCoverageSpec {
    @BeforeEach
    fun setup() {
        mockkObject(
            TaskController,
            DefaultConfigurationProvider,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            TaskController,
            DefaultConfigurationProvider,
        )
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

        val extension: AntiBytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()

        every {
            project.extensions.create("antibytesCoverage", AntiBytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
        )

        every { TaskController.configure(any(), any()) } just Runs

        // When
        AntiBytesCoverage().apply(project)

        // Then
        verify(exactly = 1) {
            project.extensions.create(
                "antibytesCoverage",
                AntiBytesCoveragePluginExtension::class.java,
                project,
            )
        }
        verify(exactly = 1) { plugins.apply("jacoco") }
        verify(exactly = 1) { project.evaluationDependsOnChildren() }
        verify(exactly = 1) { TaskController.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not override an existing contexts`() {
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context" to mockk(),
        )
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "context" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create("antibytesCoverage", AntiBytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
        )

        every { TaskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, true)
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations,
        )
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not override an existing jvm contexts`() {
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
        )
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create("antibytesCoverage", AntiBytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
        )

        every { TaskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, true)
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations,
        )
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and adds missing the jvm contexts, if the project is KMP and has no preset jvm`() {
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf()
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create("antibytesCoverage", AntiBytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
        )

        every { TaskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, true)
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations.also {
                it["jvm"] = defaultConfigurations["jvm"]!!
            },
        )
    }

    @Test
    fun `Given apply is called with a Project, it applies the JacocoPlugin, creates the Extension and will not add a jvm contexts, if the append policy is false`() {
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()
        val givenConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf()
        val defaultConfigurations: MutableMap<String, CoverageConfiguration> = mutableMapOf(
            "jvm" to mockk(),
            "context2" to mockk(),
        )

        val actualConfigurations = makeMapProperty(
            String::class.java,
            CoverageConfiguration::class.java,
            givenConfigurations.toMutableMap(),
        )

        every {
            project.extensions.create("antibytesCoverage", AntiBytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("jacoco") } returns false
        every { plugins.apply(any()) } returns mockk()

        every { project.evaluationDependsOnChildren() } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
        )

        every { TaskController.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { extension.appendKmpJvmTask } returns makeProperty(Boolean::class.java, false)
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns defaultConfigurations

        every { extension.configurations } returns actualConfigurations

        // When
        AntiBytesCoverage().apply(project)

        // Then
        assertEquals(
            actual = actualConfigurations.get(),
            expected = givenConfigurations,
        )
    }

    @Test
    fun `Given apply is called with a Project, it will not applies the JacocoPlugin, if it is already present`() {
        // Given
        val project: Project = mockk()

        val extension: AntiBytesCoveragePluginExtension = mockk()
        val plugins: PluginContainer = mockk()

        every {
            project.extensions.create("antibytesCoverage", AntiBytesCoveragePluginExtension::class.java, any())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("jacoco") } returns true
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
