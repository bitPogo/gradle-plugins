/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test

class AntiBytesKmpConfigurationSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesKmpConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project, it applies the KMP Plugin if it is not applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        AntiBytesKmpConfiguration().apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply("org.jetbrains.kotlin.multiplatform") }
    }

    @Test
    fun `Given apply is called with a Project, it applies not the  KMP Plugin if it is applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        AntiBytesKmpConfiguration().apply(project)

        // Then
        verify(exactly = 0) { pluginContainer.apply("org.jetbrains.kotlin.multiplatform") }
    }
}
