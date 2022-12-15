/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.versioning

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction
import tech.antibytes.gradle.versioning.api.VersioningConfiguration

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

    @Test
    fun `Given apply is called it does nothing if no configuration is set`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val extension = createExtension<AntiBytesVersioningPluginExtension>()
        extension.configuration.set(null)

        every { project.extensions.create(any(), AntiBytesVersioningPluginExtension::class.java) } returns extension

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            project,
        )

        // When
        AntibytesVersioning().apply(project)

        // Then
        verify(exactly = 0) { project.version = any() }
    }

    @Test
    fun `Given apply is called it sets a version if a configuration is given`() {
        mockkObject(Versioning)
        // Given
        val project: Project = mockk(relaxed = true)
        val versioning: Versioning = mockk()
        val extension = createExtension<AntiBytesVersioningPluginExtension>()
        val configuration = VersioningConfiguration()
        extension.configuration.set(configuration)
        val version: String = fixture()

        every { project.extensions.create(any(), AntiBytesVersioningPluginExtension::class.java) } returns extension
        every { Versioning.getInstance(any(), any()) } returns versioning
        every { versioning.versionName() } returns version

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project,
            project,
        )

        // When
        AntibytesVersioning().apply(project)

        // Then
        verify(exactly = 1) { project.version = version }
        verify(exactly = 1) { Versioning.getInstance(project, configuration) }

        unmockkObject(Versioning)
    }
}
