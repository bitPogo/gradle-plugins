/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
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
import tech.antibytes.gradle.configuration.android.AndroidLibraryConfigurator
import tech.antibytes.gradle.configuration.android.DefaultAndroidLibraryConfigurationProvider

class AntiBytesLibraryConfigurationSpec {
    @BeforeEach
    fun setup() {
        mockkObject(
            AndroidLibraryConfigurator,
            DefaultAndroidLibraryConfigurationProvider,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            AndroidLibraryConfigurator,
            DefaultAndroidLibraryConfigurationProvider,
        )
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesLibraryConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it applies the Android Library if it is not applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns mockk()

        // When
        AntiBytesLibraryConfiguration().apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply("com.android.library") }
    }

    @Test
    fun `Given apply is called with a Project, it applies not the Android Library if it is applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns mockk()

        // When
        AntiBytesLibraryConfiguration().apply(project)

        // Then
        verify(exactly = 0) { pluginContainer.apply("com.android.library") }
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

        val androidConfig: AndroidConfigurationApiContract.AndroidLibraryConfiguration = mockk()

        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns androidConfig

        // When
        AntiBytesLibraryConfiguration().apply(project)

        // Then
        verify(exactly = 1) { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project) }
        verify(exactly = 1) { AndroidLibraryConfigurator.configure(project, androidConfig) }
    }
}
