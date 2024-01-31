/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.doc

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.tasks.Jar
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.api.DocumentationConfiguration
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.test.createExtension

class DocumentationControllerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils DocumentationController`() {
        val controller = DocumentationController as Any

        assertTrue(controller is PublisherContract.DocumentationController)
    }

    @Test
    fun `Given configure is called it returns null if no DocumentationConfig was given`() {
        // Given
        val project: Project = mockk()
        val extension: PublishingContract.PublishingPluginExtension = createExtension()

        every { project.rootProject } returns mockk()

        // When
        val task = DocumentationController.configure(project, extension)

        // Then
        assertNull(task)
    }

    @Test
    fun `Given configure is called it returns a presetted JavaDoc Task if a DocumentationConfig was given`() {
        // Given
        val project: Project = mockk()
        val presettedTask: Task = mockk()
        val extension: PublishingContract.PublishingPluginExtension = createExtension()

        extension.documentation.set(
            DocumentationConfiguration(
                tasks = setOf("dokkaHtml"),
                outputDir = mockk(),
            ),
        )

        every { project.rootProject } returns mockk()
        every { project.tasks.findByName("javadoc") } returns presettedTask

        // When
        val task = DocumentationController.configure(project, extension)

        // Then
        assertSame(
            actual = task,
            expected = presettedTask,
        )
    }

    @Test
    fun `Given configure is called it returns a adds a JavaDoc Task if a DocumentationConfig was given and if JavaDoc Tasks was presetted`() {
        // Given
        val project: Project = mockk()
        val createdTask: Jar = mockk(relaxed = true)
        val tasks: TaskContainer = mockk()
        val extension: PublishingContract.PublishingPluginExtension = createExtension()

        extension.documentation.set(
            DocumentationConfiguration(
                tasks = setOf(fixture()),
                outputDir = mockk(relaxed = true),
            ),
        )

        every { project.rootProject } returns mockk()
        every { project.tasks } returns tasks
        every { tasks.findByName("javadoc") } returns null
        every { tasks.create(any(), any<Class<Jar>>()) } returns createdTask

        // When
        val task = DocumentationController.configure(project, extension)

        // Then
        assertSame(
            actual = task,
            expected = createdTask,
        )

        verify(exactly = 1) { createdTask.group = "Documentation" }
        verify(exactly = 1) { createdTask.description = "Generates the JavaDocs" }
        verify(exactly = 1) { createdTask.setDependsOn(extension.documentation.get().tasks) }
        verify(exactly = 1) { createdTask.archiveClassifier.set("javadoc") }
        verify(exactly = 1) { createdTask.from(extension.documentation.get().outputDir.absolutePath) }
    }
}
