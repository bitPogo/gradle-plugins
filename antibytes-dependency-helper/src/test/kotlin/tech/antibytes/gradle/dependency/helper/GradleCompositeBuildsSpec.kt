/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.initialization.IncludedBuild
import org.gradle.api.tasks.TaskContainer
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.invokeGradleAction

class GradleCompositeBuildsSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = GradleCompositeBuilds

        assertTrue(configurator is DependencyContract.Configurator)
    }

    @Test
    fun `Given configure is called it adds the cleanup tasks of the composite builds`() {
        // Given
        val compositeBuildName: String = fixture()
        val clean: Task = mockk(relaxed = true)
        val compositeBuild: IncludedBuild = mockk {
            every { name } returns compositeBuildName
            every { task(any()) } returns mockk()
        }
        val taskContainer: TaskContainer = mockk()

        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { gradle.includedBuilds } returns listOf(compositeBuild)
            every { gradle.includedBuild(any()) } returns compositeBuild
        }

        invokeGradleAction(
            { probe -> taskContainer.named(any(), probe) },
            clean,
            mockk(),
        )

        // When
        GradleCompositeBuilds.configure(project)

        // Then
        verify(atLeast = 1) { project.gradle.includedBuild(compositeBuildName) }
        verify(exactly = 1) { compositeBuild.task(":clean") }
    }
}