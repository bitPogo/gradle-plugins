/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.publishing.createExtension
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AntiBytesConfigurationPluginExtensionSpec {
    @Before
    fun setUp() {
        mockkObject(DefaultAndroidLibraryConfigurationProvider)
        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns null
    }

    @After
    fun tearDown() {
        unmockkObject(DefaultAndroidLibraryConfigurationProvider)
    }

    @Test
    fun `It fulfils ConfigurationPluginExtension`() {
        val extension: Any = createExtension<AntiBytesConfigurationPluginExtension>(mockk<Project>(relaxed = true))

        assertTrue(extension is ConfigurationContract.ConfigurationPluginExtension)
    }

    @Test
    fun `Given the extension is initialized, it has the a default Configuration given by an Provider`() {
        // Given
        val project: Project = mockk()
        val config: ConfigurationApiContract.AndroidLibraryConfiguration = mockk()

        every { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(any()) } returns config

        // When
        val extension = createExtension<AntiBytesConfigurationPluginExtension>(project)

        // Then
        assertSame(
            actual = extension.android,
            expected = config
        )

        verify(exactly = 1) { DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project) }
    }
}
