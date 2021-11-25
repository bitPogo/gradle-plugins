/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.gradle.api.Project
import org.junit.After
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.publishing.createExtension
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AntiBytesCoveragePluginExtensionSpec {
    @Before
    fun setup() {
        mockkObject(DefaultConfigurationProvider)
    }

    @After
    fun tearDown() {
        unmockkObject(DefaultConfigurationProvider)
    }

    @Test
    fun `It fulfils CoveragePluginExtension`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension: Any = createExtension<AntiBytesCoveragePluginExtension>(mockk<Project>(relaxed = true))

        assertTrue(extension is CoverageContract.CoveragePluginExtension)
    }

    @Test
    fun `It has a default Configuration if it is not the RootProject`() {
        val project: Project = mockk(relaxed = true)
        val config: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mockk()

        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(project) } returns config

        val extension = createExtension<AntiBytesCoveragePluginExtension>(project)

        assertSame(
            actual = extension.configurations,
            expected = config
        )
    }

    @Test
    fun `It has no default Configuration if the RootProject`() {
        val project: Project = mockk()
        val config: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mockk()

        every { project.rootProject } returns project
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(project) } returns config

        val extension = createExtension<AntiBytesCoveragePluginExtension>(project)

        assertEquals(
            actual = extension.configurations,
            expected = emptyMap()
        )
    }

    @Test
    fun `It has a default Jacoco version`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntiBytesCoveragePluginExtension>(mockk<Project>(relaxed = true))

        assertSame(
            actual = extension.jacocoVersion,
            expected = "0.8.7"
        )
    }

    @Test
    fun `It has a default Jvm append policy`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntiBytesCoveragePluginExtension>(mockk<Project>(relaxed = true))

        assertTrue(extension.appendKmpJvmTask)
    }
}
