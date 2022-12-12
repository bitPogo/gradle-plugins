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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.android.AndroidApplicationConfigurator
import tech.antibytes.gradle.configuration.android.AndroidLibraryConfigurator
import tech.antibytes.gradle.configuration.android.DefaultAndroidApplicationConfigurationProvider
import tech.antibytes.gradle.configuration.android.DefaultAndroidLibraryConfigurationProvider
import tech.antibytes.gradle.configuration.docs.DokkaConfigurator

class AntiBytesConfigurationSpec {
    @BeforeEach
    fun setup() {
        mockkObject(
            AndroidLibraryConfigurator,
            AndroidApplicationConfigurator,
            DefaultAndroidApplicationConfigurationProvider,
            DefaultAndroidLibraryConfigurationProvider,
            DokkaConfigurator,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(
            AndroidLibraryConfigurator,
            AndroidApplicationConfigurator,
            DefaultAndroidApplicationConfigurationProvider,
            DefaultAndroidLibraryConfigurationProvider,
            DokkaConfigurator,
        )
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it will not delegate the AndroidConfiguration if it is not a Library or Application`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("org.jetbrains.dokka") } returns false

        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns mockk()

        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 0) { AndroidLibraryConfigurator.configure(project, any()) }
    }

    @Test
    fun `Given apply is called with a Project, it will delegate the AndroidConfiguration if it is a Library`() {
        // Given
        val project: Project = mockk()

        val androidConfig: AndroidConfigurationApiContract.AndroidLibraryConfiguration = mockk()

        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns true
        every { project.plugins.hasPlugin("org.jetbrains.dokka") } returns false
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

        val androidConfig: AndroidConfigurationApiContract.AndroidApplicationConfiguration = mockk()

        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns true
        every { project.plugins.hasPlugin("org.jetbrains.dokka") } returns false
        every { AndroidApplicationConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(any()) } returns androidConfig

        // When
        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 1) { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(project) }
        verify(exactly = 1) { AndroidApplicationConfigurator.configure(project, androidConfig) }
    }

    @Test
    fun `Given apply is called with a Project it ignores Dokka if it is not enabled`() {
        // Given
        val project: Project = mockk()

        val androidConfig: AndroidConfigurationApiContract.AndroidApplicationConfiguration = mockk()

        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("org.jetbrains.dokka") } returns false
        every { AndroidApplicationConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(any()) } returns androidConfig

        // When
        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 0) { DokkaConfigurator.configure(project, any()) }
    }

    @Test
    fun `Given apply is called with a Project it configures Dokka if it is enabled`() {
        // Given
        val project: Project = mockk()

        val androidConfig: AndroidConfigurationApiContract.AndroidApplicationConfiguration = mockk()

        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("org.jetbrains.dokka") } returns true
        every { AndroidApplicationConfigurator.configure(any(), any()) } just Runs
        every { DefaultAndroidApplicationConfigurationProvider.createDefaultConfiguration(any()) } returns androidConfig
        every { DokkaConfigurator.configure(any(), any()) } just Runs

        // When
        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 1) { DokkaConfigurator.configure(project, any()) }
    }
}
