/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class AntiBytesDependencySpec {
    @Before
    fun setup() {
        mockkObject(DependencyUpdate)
    }

    @After
    fun tearDown() {
        unmockkObject(DependencyUpdate)
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesDependency()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project it creates the extension and applies the DependencyUpdate configuration`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val plugins: PluginContainer = mockk()
        val extension: AntiBytesDependencyExtension = mockk()

        every { project.extensions } returns extensions
        every {
            extensions.create("antiBytesDependency", any<Class<*>>())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns true
        every { DependencyUpdate.configure(project, extension) } just Runs

        // When
        AntiBytesDependency().apply(project)

        // Then
        verify(exactly = 1) { extensions.create("antiBytesDependency", any<Class<*>>()) }
        verify(exactly = 1) { DependencyUpdate.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project it invokes the dependency update plugin and applies the DependencyUpdate configuration`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val plugins: PluginContainer = mockk()
        val extension: AntiBytesDependencyExtension = mockk()

        every { project.extensions } returns extensions
        every {
            extensions.create("antiBytesDependency", any<Class<*>>())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("com.github.ben-manes.versions") } returns false
        every { plugins.apply("com.github.ben-manes.versions") } returns mockk()
        every { DependencyUpdate.configure(project, extension) } just Runs

        // When
        AntiBytesDependency().apply(project)

        // Then
        verify(exactly = 1) { plugins.apply("com.github.ben-manes.versions") }
        verify(exactly = 1) { DependencyUpdate.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project it will not invokes the dependency update plugin if it was already applied and applies the DependencyUpdate configuration`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val plugins: PluginContainer = mockk()
        val extension: AntiBytesDependencyExtension = mockk()

        every { project.extensions } returns extensions
        every {
            extensions.create("antiBytesDependency", any<Class<*>>())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin("com.github.ben-manes.versions") } returns true
        every { plugins.apply("com.github.ben-manes.versions") } returns mockk()
        every { DependencyUpdate.configure(project, extension) } just Runs

        // When
        AntiBytesDependency().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("com.github.ben-manes.versions") }
        verify(exactly = 1) { DependencyUpdate.configure(project, extension) }
    }
}