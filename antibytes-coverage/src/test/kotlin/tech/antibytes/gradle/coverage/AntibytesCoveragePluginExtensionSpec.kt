/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.test.createExtension

class AntibytesCoveragePluginExtensionSpec {
    private val defaultConfiguration: DefaultConfigurationProvider = mockk()

    @BeforeEach
    fun setup() {
        clearMocks(defaultConfiguration)
    }

    @Test
    fun `It fulfils CoveragePluginExtension`() {
        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns mockk()

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

        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns config

        val extension = createExtension<AntibytesCoveragePluginExtension>(project, defaultConfiguration)

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
        every { defaultConfiguration.createDefaultCoverageConfiguration(project) } returns config

        val extension = createExtension<AntibytesCoveragePluginExtension>(project, defaultConfiguration)

        assertEquals(
            actual = extension.configurations.get(),
            expected = emptyMap(),
        )
    }

    @Test
    fun `It has a default Jacoco version`() {
        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntibytesCoveragePluginExtension>(mockk<Project>(relaxed = true), defaultConfiguration)

        assertSame(
            actual = extension.jacocoVersion.get(),
            expected = "0.8.11",
        )
    }

    @Test
    fun `It has a default Jvm append policy`() {
        every { defaultConfiguration.createDefaultCoverageConfiguration(any()) } returns mockk()

        val extension = createExtension<AntibytesCoveragePluginExtension>(mockk<Project>(relaxed = true), defaultConfiguration)

        assertTrue(extension.appendKmpJvmTask.get())
    }
}
