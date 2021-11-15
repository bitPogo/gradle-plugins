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
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AntiBytesCoverageExtensionSpec {
    @Before
    fun setup() {
        mockkObject(DefaultConfigurationProvider)
    }

    @After
    fun tearDown() {
        unmockkObject(DefaultConfigurationProvider)
    }

    @Test
    fun `It fulfils Extension`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension: Any = createExtension<AntiBytesCoverageExtension>(mockk<Project>())

        assertTrue(extension is CoverageContract.Extension)
    }

    @Test
    fun `It has the default provided Configuration`() {
        val project: Project = mockk()
        val config: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mockk()

        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(project) } returns config

        val extension = createExtension<AntiBytesCoverageExtension>(project)

        assertSame(
            actual = extension.coverageConfigurations,
            expected = config
        )
    }

    @Test
    fun `It has a default Jacoco version`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntiBytesCoverageExtension>(mockk<Project>())

        assertSame(
            actual = extension.jacocoVersion,
            expected = "0.8.7"
        )
    }

    @Test
    fun `It has a default Jvm append policy`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntiBytesCoverageExtension>(mockk<Project>())

        assertTrue(extension.appendKmpJvmTask)
    }
}
