/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.publisher.PublisherController
import kotlin.test.assertTrue

class AntiBytesPublishingSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesPublishing()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called  with a Project, it creates the PluginExtension, apply the plugin dependencies and delegates it to the Publishing Controller`() {
        mockkObject(PublisherController)
        // Given
        val project: Project = mockk()
        val plugins: PluginContainer = mockk()
        val extensionContainer: ExtensionContainer = mockk()
        val extension: AntiBytesPublishingPluginExtension = mockk()

        every { project.extensions } returns extensionContainer
        every { project.plugins } returns plugins
        every { project.rootProject } returns mockk()

        every { plugins.hasPlugin("com.palantir.git-version") } returns false
        every { plugins.hasPlugin("maven-publish") } returns false
        every { plugins.apply("com.palantir.git-version") } returns mockk()
        every { plugins.apply("maven-publish") } returns mockk()

        every {
            extensionContainer.create(
                "antiBytesPublishing",
                AntiBytesPublishingPluginExtension::class.java
            )
        } returns extension
        every { PublisherController.configure(project, "", extension) } just Runs

        // When
        AntiBytesPublishing().apply(project)

        // Then
        verify(exactly = 1) {
            extensionContainer.create(
                "antiBytesPublishing",
                AntiBytesPublishingPluginExtension::class.java
            )
        }
        verify(exactly = 1) { PublisherController.configure(project, "", extension) }
        verify(exactly = 1) { plugins.apply("com.palantir.git-version") }
        verify(exactly = 1) { plugins.apply("maven-publish") }

        unmockkObject(PublisherController)
    }

    @Test
    fun `Given apply is called  with a Project, it creates the PluginExtension, ignores apply the plugin dependencies, if they already are applied and delegates it to the Publishing Controller`() {
        mockkObject(PublisherController)
        // Given
        val project: Project = mockk()
        val plugins: PluginContainer = mockk()
        val extensionContainer: ExtensionContainer = mockk()
        val extension: AntiBytesPublishingPluginExtension = mockk()

        every { project.extensions } returns extensionContainer
        every { project.plugins } returns plugins
        every { project.rootProject } returns mockk()

        every { plugins.hasPlugin("com.palantir.git-version") } returns true
        every { plugins.hasPlugin("maven-publish") } returns true
        every { plugins.apply("com.palantir.git-version") } returns mockk()
        every { plugins.apply("maven-publish") } returns mockk()

        every {
            extensionContainer.create(
                "antiBytesPublishing",
                AntiBytesPublishingPluginExtension::class.java
            )
        } returns extension
        every { PublisherController.configure(project, "", extension) } just Runs

        // When
        AntiBytesPublishing().apply(project)

        // Then
        verify(exactly = 1) {
            extensionContainer.create(
                "antiBytesPublishing",
                AntiBytesPublishingPluginExtension::class.java
            )
        }
        verify(exactly = 1) { PublisherController.configure(project, "", extension) }
        verify(exactly = 0) { plugins.apply("com.palantir.git-version") }
        verify(exactly = 0) { plugins.apply("maven-publish") }

        unmockkObject(PublisherController)
    }
}
