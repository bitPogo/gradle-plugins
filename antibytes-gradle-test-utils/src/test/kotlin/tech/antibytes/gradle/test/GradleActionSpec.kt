/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */
package tech.antibytes.gradle.test

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import kotlin.test.assertSame
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.DirectoryProperty
import org.junit.jupiter.api.Test

class GradleActionSpec {
    @Test
    fun `Given invokeGradleAction is called with a caller, a probe and result it simulates the execution`() {
        // Given
        val givenFiles: ConfigurableFileTree = mockk()
        val project: Project = mockk()
        val buildDir: DirectoryProperty = mockk()
        val fileTreeProbe: ConfigurableFileTree = mockk()
        val proof = setOf("")

        every { project.layout.buildDirectory } returns buildDir
        every { fileTreeProbe.setExcludes(proof) } returns mockk()

        // When
        invokeGradleAction(
            fileTreeProbe,
            givenFiles,
        ) { probe -> project.fileTree(buildDir, probe) }

        val result = project.fileTree(project.layout.buildDirectory) {
            setExcludes(proof)
        }

        // Then
        assertSame(
            expected = givenFiles,
            actual = result,
        )

        verify(exactly = 1) { fileTreeProbe.setExcludes(proof) }
    }

    @Test
    fun `Given invokeGradleAction is called with a caller and a probe it simulates the execution`() {
        val project: Project = mockk()
        val scopedProject: Project = mockk()
        val proof: File = mockk()

        every { scopedProject.layout.buildDirectory.set(proof) } just Runs

        // When
        invokeGradleAction(
            scopedProject,
        ) { probe -> project.afterEvaluate(probe) }

        project.afterEvaluate {
            layout.buildDirectory.set(proof)
        }

        // Then
        verify(exactly = 1) { scopedProject.layout.buildDirectory.set(proof) }
    }
}
