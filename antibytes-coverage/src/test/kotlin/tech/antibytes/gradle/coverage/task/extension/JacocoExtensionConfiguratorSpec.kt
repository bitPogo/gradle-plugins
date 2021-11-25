/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.extension

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.junit.Test
import tech.antibytes.gradle.coverage.AntiBytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.task.TaskContract
import kotlin.test.assertTrue

class JacocoExtensionConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils JacocoExtensionConfigurator`() {
        val extension: Any = JacocoExtensionConfigurator

        assertTrue(extension is TaskContract.JacocoExtensionConfigurator)
    }

    @Test
    fun `Given configure is called with a Project and a AntiBytesCoverageExtension, it configures the version of Jacoco`() {
        // Given
        val version: String = fixture()
        val project: Project = mockk()
        val configuration: AntiBytesCoveragePluginExtension = mockk()

        val jacocoExtension: JacocoPluginExtension = mockk()

        every { project.extensions.getByType(JacocoPluginExtension::class.java) } returns jacocoExtension
        every { configuration.jacocoVersion } returns version
        every { jacocoExtension.toolVersion = any() } just Runs

        // When
        JacocoExtensionConfigurator.configure(project, configuration)

        // Then
        verify(exactly = 1) { jacocoExtension.toolVersion = version }
    }
}
