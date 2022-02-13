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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class AntiBytesConfigurationSpec {
    @BeforeEach
    fun setup() {
        mockkObject(AndroidLibraryConfigurator)
        mockkObject(AndroidApplicationConfigurator)
        mockkObject(DefaultAndroidApplicationConfigurationProvider)
        mockkObject(DefaultAndroidLibraryConfigurationProvider)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(AndroidLibraryConfigurator)
        unmockkObject(AndroidApplicationConfigurator)
        unmockkObject(DefaultAndroidApplicationConfigurationProvider)
        unmockkObject(DefaultAndroidLibraryConfigurationProvider)
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it will not delegate the AndroidConfiguration if it is not a Library or Application`() {
        mockkObject(AndroidLibraryConfigurator)
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false

        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns mockk()

        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 0) { AndroidLibraryConfigurator.configure(project, any()) }

        unmockkObject(AndroidLibraryConfigurator)
    }

    @Test
    fun `Given apply is called with a Project, it will delegate the AndroidConfiguration if it is a Library`() {
        // Given
        val project: Project = mockk()

        val androidConfig: ConfigurationApiContract.AndroidLibraryConfiguration = mockk()

        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns true
        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns androidConfig

        // When
        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 1) { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project) }
        verify(exactly = 1) { AndroidLibraryConfigurator.configure(project, androidConfig) }
    }

    @Test
    fun `Given apply is called with a Project, it will delegate the AndroidConfiguration if it is a Application`() {
        // Given
        val project: Project = mockk()

        val androidConfig: ConfigurationApiContract.AndroidApplicationConfiguration = mockk()

        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns true
        every { AndroidApplicationConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(any()) } returns androidConfig

        // When
        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 1) { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(project) }
        verify(exactly = 1) { AndroidApplicationConfigurator.configure(project, androidConfig) }
    }
}
