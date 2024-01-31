/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

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
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.docs.DokkaConfigurator

class AntibytesDokkaConfigurationSpec {
    @BeforeEach
    fun setup() {
        mockkObject(
            DokkaConfigurator,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            DokkaConfigurator,
        )
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesDokkaConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it applies the Dokka Plugin if it is not applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        every { DokkaConfigurator.configure(any(), any()) } just Runs

        // When
        AntibytesDokkaConfiguration().apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply("org.jetbrains.dokka") }
    }

    @Test
    fun `Given apply is called with a Project, it applies not the Dokka Plugin if it is applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        every { DokkaConfigurator.configure(any(), any()) } just Runs

        // When
        AntibytesDokkaConfiguration().apply(project)

        // Then
        verify(exactly = 0) { pluginContainer.apply("org.jetbrains.dokka") }
    }

    @Test
    fun `Given apply is called with a Project, it configures Dokka`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        every { DokkaConfigurator.configure(any(), any()) } just Runs

        // When
        AntibytesDokkaConfiguration().apply(project)

        // Then
        verify(exactly = 1) { DokkaConfigurator.configure(project, any()) }
    }
}
