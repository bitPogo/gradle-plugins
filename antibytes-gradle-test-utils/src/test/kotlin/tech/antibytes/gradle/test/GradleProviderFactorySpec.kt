/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.test

import java.util.UUID
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.GradleProviderFactory.createProvider
import tech.antibytes.gradle.test.GradleProviderFactory.createTaskProvider

class GradleProviderFactorySpec {
    @Test
    fun `Given createProvider is called it returns a provider with the given value`() {
        // Given
        val value: String = UUID.randomUUID().toString()

        // When
        val provider: Provider<String> = createProvider(value)

        // Then
        assertTrue { provider is Provider<*> }
        assertEquals(
            provider.get(),
            value,
        )
    }

    @Test
    fun `Given createTaskProvider is called it returns a provider with the given value`() {
        // Given
        val name = UUID.randomUUID().toString()
        val parameter = UUID.randomUUID().toString()

        // When
        val provider: TaskProvider<ATask> = createTaskProvider(name, ATask::class) {
            proof = "yes"
        }

        // Then
        assertTrue { provider is TaskProvider<*> }
        assertTrue { provider.get() is ATask }
        assertEquals(
            provider.get().proof,
            "yes",
        )
    }

    @Test
    fun `Given createTaskProvider is called with parameter it returns a provider with the given value`() {
        // Given
        val name = UUID.randomUUID().toString()
        val parameter = UUID.randomUUID().toString()

        // When
        val provider: TaskProvider<AParameterizedTask> = createTaskProvider(name, AParameterizedTask::class, parameter) {
            proof = "yes"
        }

        // Then
        assertTrue { provider is TaskProvider<*> }
        assertTrue { provider.get() is AParameterizedTask }
        assertEquals(
            provider.get().parameter,
            parameter,
        )
        assertEquals(
            provider.get().proof,
            "yes",
        )
    }

    @Test
    fun `Given createTaskProvider is called with a project it returns a provider with the given value`() {
        // Given
        val name = UUID.randomUUID().toString()
        val project = ProjectBuilder.builder().build()

        // When
        val provider: TaskProvider<ATask> = createTaskProvider(project, name, ATask::class) {
            proof = "yes"
        }

        // Then
        assertTrue { provider is TaskProvider<*> }
        assertTrue { provider.get() is ATask }
        assertEquals(
            provider.get().proof,
            "yes",
        )
        assertNotNull(project.tasks.named(name).get())
    }

    @Test
    fun `Given createTaskProvider is called with a project and parameter it returns a provider with the given value`() {
        // Given
        val project = ProjectBuilder.builder().build()
        val name = UUID.randomUUID().toString()
        val parameter = UUID.randomUUID().toString()

        // When
        val provider: TaskProvider<AParameterizedTask> = createTaskProvider(
            project,
            name,
            AParameterizedTask::class,
            parameter,
        ) {
            proof = "yes"
        }

        // Then
        assertTrue { provider is TaskProvider<*> }
        assertTrue { provider.get() is AParameterizedTask }
        assertEquals(
            provider.get().parameter,
            parameter,
        )
        assertEquals(
            provider.get().proof,
            "yes",
        )
        assertNotNull(project.tasks.named(name).get())
    }
}

private abstract class ATask : DefaultTask(), Task {
    var proof: String = "no"
}

private abstract class AParameterizedTask @Inject constructor(
    val parameter: String,
) : DefaultTask(), Task {
    var proof: String = "no"
}
