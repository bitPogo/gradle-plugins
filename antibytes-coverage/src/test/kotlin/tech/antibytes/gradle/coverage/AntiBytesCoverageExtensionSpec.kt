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
import tech.antibytes.gradle.coverage.configuration.DefaultCoverageProvider
import tech.antibytes.gradle.publishing.createExtension
import kotlin.test.assertSame
import kotlin.test.assertTrue

class AntiBytesCoverageExtensionSpec {
    @Before
    fun setup() {
        mockkObject(DefaultCoverageProvider)
    }

    @After
    fun tearDown() {
        unmockkObject(DefaultCoverageProvider)
    }

    @Test
    fun `It fulfils Extension`() {
        every { DefaultCoverageProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension: Any = createExtension<AntiBytesCoverageExtension>(mockk<Project>())

        assertTrue(extension is CoverageContract.Extension)
    }

    @Test
    fun `It has the default provided Configuration`() {
        val project: Project = mockk()
        val config: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mockk()

        every { DefaultCoverageProvider.createDefaultCoverageConfiguration(project) } returns config

        val extension = createExtension<AntiBytesCoverageExtension>(project)

        assertSame(
            actual = extension.coverageConfigurations,
            expected = config
        )
    }

    @Test
    fun `It has a default Jacoco version`() {
        every { DefaultCoverageProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntiBytesCoverageExtension>(mockk<Project>())

        assertSame(
            actual = extension.jacocoVersion,
            expected = "0.8.7"
        )
    }
}
