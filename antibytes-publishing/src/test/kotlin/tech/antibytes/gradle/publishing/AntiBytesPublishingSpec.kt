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
import org.junit.Test
import kotlin.test.assertTrue

class AntiBytesPublishingSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesPublishing()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `Given apply is called  with a Project, it creates the PluginExtension and delegates it to the Publishing Controller`() {
        mockkObject(PublisherController)
        // Given
        val project: Project = mockk()
        val extensionContainer: ExtensionContainer = mockk()
        val extension: AntiBytesPublishingPluginExtension = mockk()

        every { project.extensions } returns extensionContainer
        every {
            extensionContainer.create(
                "antiBytesPublishing",
                AntiBytesPublishingPluginExtension::class.java
            )
        } returns extension
        every { PublisherController.configure(project, extension) } just Runs

        // When
        AntiBytesPublishing().apply(project)

        // Then
        verify(exactly = 1) {
            extensionContainer.create(
                "antiBytesPublishing",
                AntiBytesPublishingPluginExtension::class.java
            )
        }
        verify(exactly = 1) { PublisherController.configure(project, extension) }

        unmockkObject(PublisherController)
    }
}
