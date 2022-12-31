/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.QualityContract.Companion.EXTENSION_ID
import tech.antibytes.gradle.quality.analysis.Detekt
import tech.antibytes.gradle.quality.gate.Sonarqube
import tech.antibytes.gradle.quality.linter.Spotless
import tech.antibytes.gradle.quality.stableapi.StableApi
import tech.antibytes.gradle.test.createExtension
import tech.antibytes.gradle.test.invokeGradleAction

class AntibytesQualitySpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntibytesQuality()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called it applies the DependencyHelper Plugin if it is not already applied`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)

        every { project.extensions.create(EXTENSION_ID, AntibytesQualityExtension::class.java) } returns mockk()
        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns false

        // When
        AntibytesQuality().apply(project)

        // Then
        verify(exactly = 1) { plugins.apply("tech.antibytes.gradle.dependency.helper") }
    }

    @Test
    fun `Given apply is called it ignores the DependencyHelper Plugin if it is already applied`() {
        // Given
        val project: Project = mockk(relaxed = true)
        val plugins: PluginContainer = mockk(relaxed = true)

        every { project.extensions.create(EXTENSION_ID, AntibytesQualityExtension::class.java) } returns mockk()
        every { project.plugins } returns plugins
        every { plugins.hasPlugin(any<String>()) } returns true

        // When
        AntibytesQuality().apply(project)

        // Then
        verify(exactly = 0) { plugins.apply("tech.antibytes.gradle.dependency.helper") }
    }

    @Test
    fun `Given apply is called it creates an Extension and delegates it to the phases`() {
        mockkObject(
            Spotless,
            Detekt,
            Sonarqube,
            StableApi,
        )
        // Given
        val project: Project = mockk(relaxed = true)
        val extension = createExtension<AntibytesQualityExtension>()

        every { Spotless.configure(any(), any()) } just Runs
        every { Detekt.configure(any(), any()) } just Runs
        every { Sonarqube.configure(any(), any()) } just Runs
        every { StableApi.configure(any(), any()) } just Runs
        every {
            project.extensions.create(EXTENSION_ID, AntibytesQualityExtension::class.java)
        } returns extension

        invokeGradleAction(
            project,
            project,
        ) { probe ->
            project.afterEvaluate(probe)
        }

        // When
        AntibytesQuality().apply(project)

        // Then
        verify(exactly = 1) { Spotless.configure(project, extension) }
        verify(exactly = 1) { Detekt.configure(project, extension) }
        verify(exactly = 1) { Sonarqube.configure(project, extension) }
        verify(exactly = 1) { StableApi.configure(project, extension) }

        unmockkObject(
            Spotless,
            Detekt,
            Sonarqube,
            StableApi,
        )
    }
}
