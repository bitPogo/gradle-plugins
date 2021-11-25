/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.junit.Test
import tech.antibytes.gradle.publishing.invokeGradleAction
import kotlin.test.assertTrue

class AntiBytesConfigurationSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called with a Project it creates a Extension`() {
        // Given
        val project: Project = mockk()

        // When
        every {
            project.extensions.create(
                "antibytesProjectConfiguration",
                AntiBytesConfigurationPluginExtension::class.java,
                project
            )
        } returns mockk()
        every { project.afterEvaluate(any<Action<Project>>()) } returns mockk()

        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 1) {
            project.extensions.create(
                "antibytesProjectConfiguration",
                AntiBytesConfigurationPluginExtension::class.java,
                project
            )
        }
    }

    @Test
    fun `Given apply is called with a Project, it will not delegate the AndroidConfiguration if it is not a Library`() {
        mockkObject(AndroidLibraryConfigurator)
        // Given
        val project: Project = mockk()

        val extension: AntiBytesConfigurationPluginExtension = mockk(relaxed = true)
        // When
        every {
            project.extensions.create(
                "antibytesProjectConfiguration",
                AntiBytesConfigurationPluginExtension::class.java,
                project
            )
        } returns extension

        every { extension.android } returns null
        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project
        )

        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 0) { AndroidLibraryConfigurator.configure(project, any()) }

        unmockkObject(AndroidLibraryConfigurator)
    }

    @Test
    fun `Given apply is called with a Project, it will delegate the AndroidConfiguration if it is a Library`() {
        mockkObject(AndroidLibraryConfigurator)
        // Given
        val project: Project = mockk()

        val extension: AntiBytesConfigurationPluginExtension = mockk()
        val androidConfig: ConfigurationApiContract.AndroidLibraryConfiguration = mockk()
        // When
        every {
            project.extensions.create(
                "antibytesProjectConfiguration",
                AntiBytesConfigurationPluginExtension::class.java,
                project
            )
        } returns extension

        every { extension.android } returns androidConfig
        every { AndroidLibraryConfigurator.configure(any(), any()) } just Runs

        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            project
        )

        AntiBytesConfiguration().apply(project)

        // Then
        verify(exactly = 1) { AndroidLibraryConfigurator.configure(project, androidConfig) }

        unmockkObject(AndroidLibraryConfigurator)
    }
}
