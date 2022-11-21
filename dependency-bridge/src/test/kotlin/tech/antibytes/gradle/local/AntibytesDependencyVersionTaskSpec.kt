/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import tech.antibytes.gradle.local.DependencyVersionContract.DependencyVersionTask
import kotlin.test.assertFailsWith

class AntibytesDependencyVersionTaskSpec {
    @TempDir
    private lateinit var buildDir: File
    @TempDir
    private lateinit var dependencyDir1: File
    @TempDir
    private lateinit var dependencyDir2: File
    private lateinit var project: Project
    private val fixtureRoot = "/generated"

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.buildDir = buildDir
    }

    private fun loadResource(path: String): String {
        return this::class.java.getResource(fixtureRoot + path)!!.readText()
    }

    private fun String.normalizeSource(): String {
        return this.replace(Regex("[\t ]+"), "").replace(Regex("[\n]+"), "\n")
    }

    @Test
    fun `It fulfils DefaultPlugin`() {
        val task: Any = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        assert(task is DefaultTask)
    }

    @Test
    fun `It fulfils RuntimeConfigurationTask`() {
        val task: Any = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        assert(task is DependencyVersionTask)
    }

    @Test
    fun `Given the task is executed it fails if no packageName is set`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        // Then
        assertFailsWith<StopExecutionException> {
            // When
            task.generate()
        }
    }
}
