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
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertSame

class GradleActionSpec {
    @Test
    fun `Given invokeGradleAction is called with a caller, a probe and result it simulates the execution`() {
        // Given
        val givenFiles: ConfigurableFileTree = mockk()
        val project: Project = mockk()
        val buildDir: File = mockk()
        val fileTreeProbe: ConfigurableFileTree = mockk()
        val proof = setOf("")

        every { project.buildDir } returns buildDir
        every { fileTreeProbe.setExcludes(proof) } returns mockk()

        // When
        invokeGradleAction(
            { probe -> project.fileTree(buildDir, probe) },
            fileTreeProbe,
            givenFiles
        )

        val result = project.fileTree(project.buildDir) {
            setExcludes(proof)
        }

        // Then
        assertSame(
            expected = givenFiles,
            actual = result
        )

        verify(exactly = 1) { fileTreeProbe.setExcludes(proof) }
    }

    @Test
    fun `Given invokeGradleAction is called with a caller and a probe it simulates the execution`() {
        val project: Project = mockk()
        val scopedProject: Project = mockk()
        val proof: File = mockk()

        every { scopedProject.buildDir = proof } just Runs

        // When
        invokeGradleAction(
            { probe -> project.afterEvaluate(probe) },
            scopedProject
        )

        project.afterEvaluate {
            buildDir = proof
        }

        // Then
        verify(exactly = 1) { scopedProject.buildDir = proof }
    }
}
