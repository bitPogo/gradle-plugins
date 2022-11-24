/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
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
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AntiBytesDependencyHelperSpec {
    @BeforeEach
    fun setup() {
        mockkObject(DependencyUpdate)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(DependencyUpdate)
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesDependencyHelper()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project it creates the extension and applies the DependencyUpdate configuration`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val plugins: PluginContainer = mockk()
        val extension: AntiBytesDependencyPluginExtension = mockk()

        every { project.extensions } returns extensions
        every {
            extensions.create("antiBytesDependency", any<Class<*>>())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns true
        every { DependencyUpdate.configure(project, extension) } just Runs

        // When
        AntiBytesDependencyHelper().apply(project)

        // Then
        verify(exactly = 1) { extensions.create("antiBytesDependency", any<Class<*>>()) }
        verify(exactly = 1) { DependencyUpdate.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project it invokes the dependency update plugin and applies the DependencyUpdate plugin`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val plugins: PluginContainer = mockk()
        val extension: AntiBytesDependencyPluginExtension = mockk()

        every { project.extensions } returns extensions
        every {
            extensions.create("antiBytesDependency", any<Class<*>>())
        } returns extension

        every { project.plugins } returns plugins

        every { plugins.hasPlugin(any<String>()) } returns true

        every { plugins.hasPlugin("com.github.ben-manes.versions") } returns false
        every { plugins.apply("com.github.ben-manes.versions") } returns mockk()
        every { DependencyUpdate.configure(project, extension) } just Runs

        // When
        AntiBytesDependencyHelper().apply(project)

        // Then
        verify(atLeast = 1) { plugins.apply("com.github.ben-manes.versions") }
        verify(exactly = 1) { DependencyUpdate.configure(project, extension) }
    }

    @Test
    fun `Given apply is called with a Project it invokes the dependency update plugin and applies the OWASP plugin`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val plugins: PluginContainer = mockk()
        val extension: AntiBytesDependencyPluginExtension = mockk()

        every { project.extensions } returns extensions
        every {
            extensions.create("antiBytesDependency", any<Class<*>>())
        } returns extension

        every { project.plugins } returns plugins

        every { plugins.hasPlugin(any<String>()) } returns true
        every { DependencyUpdate.configure(any(), any()) } just Runs

        every { plugins.hasPlugin("org.owasp.dependencycheck") } returns false
        every { plugins.apply("org.owasp.dependencycheck") } returns mockk()

        // When
        AntiBytesDependencyHelper().apply(project)

        // Then
        verify(atLeast = 1) { plugins.apply("org.owasp.dependencycheck") }
    }

    @Test
    fun `Given apply is called with a Project it will not invokes the any plugin if it was already applied`() {
        // Given
        val project: Project = mockk()
        val extensions: ExtensionContainer = mockk()
        val plugins: PluginContainer = mockk()
        val extension: AntiBytesDependencyPluginExtension = mockk()

        every { project.extensions } returns extensions
        every {
            extensions.create("antiBytesDependency", any<Class<*>>())
        } returns extension

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns true
        every { plugins.apply(any()) } returns mockk()
        every { DependencyUpdate.configure(project, extension) } just Runs

        // When
        AntiBytesDependencyHelper().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("com.github.ben-manes.versions") }
        verify(exactly = 0) { plugins.apply("org.owasp.dependencycheck") }
        verify(exactly = 0) { plugins.apply("com.diffplug.spotless") }
        verify(exactly = 1) { DependencyUpdate.configure(project, extension) }
    }
}
