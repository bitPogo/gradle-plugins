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
import tech.antibytes.gradle.configuration.android.AndroidApplicationConfigurator
import tech.antibytes.gradle.configuration.android.CompileTaskConfigurator
import tech.antibytes.gradle.configuration.android.DefaultAndroidApplicationConfigurationProvider
import tech.antibytes.gradle.configuration.android.ToolChainConfigurator

class AntibytesApplicationConfigurationSpec {
    @BeforeEach
    fun setup() {
        mockkObject(
            AndroidApplicationConfigurator,
            DefaultAndroidApplicationConfigurationProvider,
            CompileTaskConfigurator,
            ToolChainConfigurator,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            AndroidApplicationConfigurator,
            DefaultAndroidApplicationConfigurationProvider,
            CompileTaskConfigurator,
            ToolChainConfigurator,
        )
    }

    @Suppress("KotlinConstantConditions")
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesApplicationConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it applies the Android Application if it is not applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        every { AndroidApplicationConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(any()) } returns mockk()
        every { CompileTaskConfigurator.configure(any()) } just Runs
        every { ToolChainConfigurator.configure(any()) } just Runs

        // When
        AntibytesApplicationConfiguration().apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply("com.android.application") }
    }

    @Test
    fun `Given apply is called with a Project, it applies not the Android Application if it is applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        every { AndroidApplicationConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(any()) } returns mockk()
        every { CompileTaskConfigurator.configure(any()) } just Runs
        every { ToolChainConfigurator.configure(any()) } just Runs

        // When
        AntibytesApplicationConfiguration().apply(project)

        // Then
        verify(exactly = 0) { pluginContainer.apply("com.android.application") }
    }

    @Test
    fun `Given apply is called with a Project, it configures Android`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        val androidConfig: AndroidConfigurationApiContract.AndroidApplicationConfiguration = mockk()

        every { AndroidApplicationConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(any()) } returns androidConfig
        every { CompileTaskConfigurator.configure(any()) } just Runs
        every { ToolChainConfigurator.configure(any()) } just Runs

        // When
        AntibytesApplicationConfiguration().apply(project)

        // Then
        verify(exactly = 1) { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(project) }
        verify(exactly = 1) { AndroidApplicationConfigurator.configure(project, androidConfig) }
        verify(exactly = 1) { CompileTaskConfigurator.configure(project) }
        verify(exactly = 1) { ToolChainConfigurator.configure(project) }
    }
}
