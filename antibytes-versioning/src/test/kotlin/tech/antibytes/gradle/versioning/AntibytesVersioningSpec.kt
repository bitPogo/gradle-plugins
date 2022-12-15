/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension

class AntibytesVersioningSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesVersioning()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called it adds missing dependencies`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk()

        every { project.plugins } returns plugins
        every { project.extensions.create(any(), AntiBytesVersioningPluginExtension::class.java) } returns mockk()
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
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk()

        every { project.plugins } returns plugins
        every { project.extensions.create(any(), AntiBytesVersioningPluginExtension::class.java) } returns mockk()
        every { plugins.hasPlugin(any<String>()) } returns true
        every { plugins.apply(any()) } returns mockk()

        // When
        AntibytesVersioning().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("com.palantir.git-version") }
    }

    @Test
    fun `Given apply is called it creates a Extension missing dependencies`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk()
        val extension = createExtension<AntiBytesVersioningPluginExtension>(project)

        every { project.plugins } returns plugins
        every { project.extensions.create(any(), AntiBytesVersioningPluginExtension::class.java) } returns extension
        every { plugins.hasPlugin(any<String>()) } returns true
        every { plugins.apply(any()) } returns mockk()

        // When
        AntibytesVersioning().apply(project)

        // Then
        verify(exactly = 1) {
            project.extensions.create("antibytesVersioning", AntiBytesVersioningPluginExtension::class.java)
        }
    }
}
