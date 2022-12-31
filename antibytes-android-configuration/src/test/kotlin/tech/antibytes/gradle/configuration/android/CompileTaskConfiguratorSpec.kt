/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.android

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.configuration.ConfigurationContract
import tech.antibytes.gradle.test.invokeGradleAction

class CompileTaskConfiguratorSpec {
    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = CompileTaskConfigurator

        assertTrue(configurator is ConfigurationContract.Configurator<*>)
    }

    @Test
    fun `Given configure is called it adjust the compile task for Android`() {
        // Given
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val task: KotlinCompile = mockk()
        val lambdaExec = slot<KotlinJvmOptions.() -> Unit>()
        val options: KotlinJvmOptions = mockk(relaxed = true)

        every { project.tasks } returns tasks
        every { task.name } returns "releaseAndroid"
        every { task.kotlinOptions(capture(lambdaExec)) } just Runs

        invokeGradleAction(task, mockk()) { action ->
            tasks.withType(KotlinCompile::class.java, action)
        }

        // When
        CompileTaskConfigurator.configure(project, Unit)

        // Then
        lambdaExec.captured.invoke(options)

        verify(exactly = 1) { options.jvmTarget = "1.8" }
    }

    @Test
    fun `Given configure is called it ignores non Android compile tasks`() {
        // Given
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val task: KotlinCompile = mockk()
        val lambdaExec = slot<KotlinJvmOptions.() -> Unit>()

        every { project.tasks } returns tasks
        every { task.name } returns "jvm"
        every { task.kotlinOptions(capture(lambdaExec)) } just Runs

        invokeGradleAction(task, mockk()) { action ->
            tasks.withType(KotlinCompile::class.java, action)
        }

        // When
        CompileTaskConfigurator.configure(project, Unit)

        // Then
        assertFalse(lambdaExec.isCaptured)
    }
}
