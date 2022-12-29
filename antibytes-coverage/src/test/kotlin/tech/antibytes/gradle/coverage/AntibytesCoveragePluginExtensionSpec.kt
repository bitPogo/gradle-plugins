/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.test.createExtension

class AntibytesCoveragePluginExtensionSpec {
    @BeforeEach
    fun setup() {
        mockkObject(DefaultConfigurationProvider)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(DefaultConfigurationProvider)
    }

    @Test
    fun `It fulfils CoveragePluginExtension`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension: Any = createExtension<AntibytesCoveragePluginExtension>(mockk<Project>(relaxed = true))

        assertTrue(extension is CoverageContract.CoveragePluginExtension)
    }

    @Test
    fun `It has a default Configuration if it is not a RootProject`() {
        val root = ProjectBuilder.builder().build()
        val project = ProjectBuilder.builder().withParent(root).build()

        val config: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf(
            "1" to mockk(),
            "2" to mockk(),
        )

        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns config

        val extension = createExtension<AntibytesCoveragePluginExtension>(project)

        assertEquals(
            actual = extension.configurations.get(),
            expected = config,
        )
    }

    @Test
    fun `It has no default Configuration if a RootProject`() {
        val project: Project = mockk()
        val config: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mockk()

        every { project.rootProject } returns project
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(project) } returns config

        val extension = createExtension<AntibytesCoveragePluginExtension>(project)

        assertEquals(
            actual = extension.configurations.get(),
            expected = emptyMap(),
        )
    }

    @Test
    fun `It has a default Jacoco version`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntibytesCoveragePluginExtension>(mockk<Project>(relaxed = true))

        assertSame(
            actual = extension.jacocoVersion.get(),
            expected = "0.8.8",
        )
    }

    @Test
    fun `It has a default Jvm append policy`() {
        every { DefaultConfigurationProvider.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntibytesCoveragePluginExtension>(mockk<Project>(relaxed = true))

        assertTrue(extension.appendKmpJvmTask.get())
    }
}
