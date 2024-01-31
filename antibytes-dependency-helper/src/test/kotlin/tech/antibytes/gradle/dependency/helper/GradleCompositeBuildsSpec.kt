/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
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

class GradleCompositeBuildsSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = GradleCompositeBuilds

        assertTrue(configurator is DependencyContract.Configurator)
    }

    @Test
    fun `Given configure is called it ignores non existed cleanup tasks`() {
        // Given
        val compositeBuildName: String = fixture()
        val compositeBuild: IncludedBuild = mockk {
            every { name } returns compositeBuildName
            every { task(any()) } returns mockk()
        }
        val taskContainer: TaskContainer = mockk {
            every { findByName(any()) } returns null
        }

        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { gradle.includedBuilds } returns listOf(compositeBuild)
            every { gradle.includedBuild(any()) } returns compositeBuild
        }

        // When
        GradleCompositeBuilds.configure(project)

        // Then
        verify(atLeast = 0) { project.gradle.includedBuild(compositeBuildName) }
        verify(exactly = 0) { compositeBuild.task(":clean") }
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
        val taskContainer: TaskContainer = mockk {
            every { findByName(any()) } returns clean
        }

        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { gradle.includedBuilds } returns listOf(compositeBuild)
            every { gradle.includedBuild(any()) } returns compositeBuild
        }

        // When
        GradleCompositeBuilds.configure(project)

        // Then
        verify(atLeast = 1) { project.gradle.includedBuild(compositeBuildName) }
        verify(exactly = 1) { compositeBuild.task(":clean") }
    }

    @Test
    fun `Given configure is called it ignores the check tasks of composite builds by default`() {
        // Given
        val compositeBuildName: String = fixture()
        val check: Task = mockk(relaxed = true)
        val compositeBuild: IncludedBuild = mockk {
            every { name } returns compositeBuildName
            every { task(any()) } returns mockk()
        }
        val taskContainer: TaskContainer = mockk {
            every { findByName(any()) } returns check
        }

        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { gradle.includedBuilds } returns listOf(compositeBuild)
            every { gradle.includedBuild(any()) } returns compositeBuild
        }

        // When
        GradleCompositeBuilds.configure(project)

        // Then
        verify(atLeast = 0) { project.gradle.includedBuild(compositeBuildName) }
        verify(exactly = 0) { compositeBuild.task(":check") }
    }

    @Test
    fun `Given configure is called it ignores the check tasks of composite builds if the deepCheck is false`() {
        // Given
        val compositeBuildName: String = fixture()
        val check: Task = mockk(relaxed = true)
        val compositeBuild: IncludedBuild = mockk {
            every { name } returns compositeBuildName
            every { task(any()) } returns mockk()
        }
        val taskContainer: TaskContainer = mockk {
            every { findByName(any()) } returns check
        }

        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { gradle.includedBuilds } returns listOf(compositeBuild)
            every { gradle.includedBuild(any()) } returns compositeBuild
        }

        // When
        GradleCompositeBuilds.configure(project, false)

        // Then
        verify(atLeast = 0) { project.gradle.includedBuild(compositeBuildName) }
        verify(exactly = 0) { compositeBuild.task(":check") }
    }

    @Test
    fun `Given configure is called it ignores the check tasks of composite builds if the deepCheck is true and no check task exists`() {
        // Given
        val compositeBuildName: String = fixture()
        val compositeBuild: IncludedBuild = mockk {
            every { name } returns compositeBuildName
            every { task(any()) } returns mockk()
        }
        val taskContainer: TaskContainer = mockk {
            every { findByName(any()) } returns null
        }

        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { gradle.includedBuilds } returns listOf(compositeBuild)
            every { gradle.includedBuild(any()) } returns compositeBuild
        }

        // When
        GradleCompositeBuilds.configure(project, true)

        // Then
        verify(atLeast = 0) { project.gradle.includedBuild(compositeBuildName) }
        verify(exactly = 0) { compositeBuild.task(":check") }
    }

    @Test
    fun `Given configure is called it adds the check tasks of composite builds if the deepCheck is true`() {
        // Given
        val compositeBuildName: String = fixture()
        val check: Task = mockk(relaxed = true)
        val compositeBuild: IncludedBuild = mockk {
            every { name } returns compositeBuildName
            every { task(any()) } returns mockk()
        }
        val taskContainer: TaskContainer = mockk {
            every { findByName(any()) } returns check
        }

        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { gradle.includedBuilds } returns listOf(compositeBuild)
            every { gradle.includedBuild(any()) } returns compositeBuild
        }

        // When
        GradleCompositeBuilds.configure(project, true)

        // Then
        verify(atLeast = 1) { project.gradle.includedBuild(compositeBuildName) }
        verify(exactly = 1) { compositeBuild.task(":check") }
    }
}
