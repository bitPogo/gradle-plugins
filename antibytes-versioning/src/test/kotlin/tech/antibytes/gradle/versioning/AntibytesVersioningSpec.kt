/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test

class AntibytesVersioningSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesVersioning()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called it adds missing dependencies`() {
        // Given
        val project: Project = mockk()
        val plugins: PluginContainer = mockk()

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns false
        every { plugins.apply(any()) } returns mockk()

        // When
        AntibytesVersioning().apply(project)

        // Then
        verify(exactly = 1) { plugins.apply("com.palantir.git-version") }
    }

    @Test
    fun `Given apply is called it ignores missing dependencies`() {
        // Given
        val project: Project = mockk()
        val plugins: PluginContainer = mockk()

        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns true
        every { plugins.apply(any()) } returns mockk()

        // When
        AntibytesVersioning().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("com.palantir.git-version") }
    }
}