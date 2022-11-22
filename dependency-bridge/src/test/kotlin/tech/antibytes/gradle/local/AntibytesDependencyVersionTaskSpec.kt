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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AntibytesDependencyVersionTaskSpec {
    @TempDir
    private lateinit var buildDir: File

    @TempDir
    private lateinit var dependencyDir1: File

    @TempDir
    private lateinit var dependencyDir2: File
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.buildDir = buildDir
    }

    private fun loadResource(path: String): String {
        return this::class.java.getResource(path)!!.readText()
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
    fun `It fulfils DependencyVersionTask`() {
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

    @Test
    fun `Given the task is executed it fails if the packageName is empty`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        task.packageName.set("")

        // Then
        assertFailsWith<StopExecutionException> {
            // When
            task.generate()
        }
    }

    @Test
    fun `Given the task is executed it generates the buildDir if it does not exists`() {
        // Given
        val buildDir = "build"
        project.buildDir = File(this.buildDir, buildDir)
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        // When
        task.packageName.set("packageName")
        task.generate()

        // Then
        assertEquals(
            actual = this.buildDir.list()!!.first(),
            expected = buildDir,
        )
    }

    @Test
    fun `Given the task is executed it ignores if there are no files`() {
        // Given
        project.buildDir = File(buildDir, "build")
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        // When
        task.packageName.set("packageName")
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("kt")) {
                pointer = file
            }
        }

        assertNull(pointer)
    }

    @Test
    fun `Given the task is executed it writes the content of a single requirements file for python`() {
        // Given
        project.buildDir = File(buildDir, "build")
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val singleRequirements = File(dependencyDir1, "requirements.txt")
        singleRequirements.createNewFile().also {
            singleRequirements.writeText(loadResource("/python/Regular.txt"))
        }

        val expected = loadResource("/task/SinglePython.kt")

        // When
        task.packageName.set("some.name.test")
        task.pythonDirectory.set(
            listOf(dependencyDir1)
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("PythonVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource()
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple requirements files for python`() {
        // Given
        project.buildDir = File(buildDir, "build")
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        val firstRequirements = File(dependencyDir1, "requirements.txt")
        firstRequirements.createNewFile().also {
            firstRequirements.writeText(loadResource("/python/Regular.txt"))
        }

        val secondRequirements = File(dependencyDir2, "requirements.txt")
        secondRequirements.createNewFile().also {
            secondRequirements.writeText(loadResource("/python/Additional.txt"))
        }

        val expected = loadResource("/task/MultiPython.kt")

        // When
        task.packageName.set("some.name.test")
        task.pythonDirectory.set(
            listOf(dependencyDir1, dependencyDir2)
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("PythonVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource()
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a single toml file for gradle`() {
        // Given
        project.buildDir = File(buildDir, "build")
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val singleRequirements = File(dependencyDir1, "dependencies.toml")
        singleRequirements.createNewFile().also {
            singleRequirements.writeText(loadResource("/gradle/versions.toml"))
        }

        val expected = loadResource("/task/SingleGradle.kt")

        // When
        task.packageName.set("some.name.test")
        task.gradleDirectory.set(
            listOf(dependencyDir1)
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("GradleVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource()
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple toml files in the same folders for gradle`() {
        // Given
        project.buildDir = File(buildDir, "build")
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        val firstRequirements = File(dependencyDir1, "dependencies.toml")
        firstRequirements.createNewFile().also {
            firstRequirements.writeText(loadResource("/gradle/versions.toml"))
        }

        val secondRequirements = File(dependencyDir1, "additional.dependencies.toml")
        secondRequirements.createNewFile().also {
            secondRequirements.writeText(loadResource("/gradle/additional.versions.toml"))
        }

        val expected = loadResource("/task/MultiGradle.kt")

        // When
        task.packageName.set("some.name.test")
        task.gradleDirectory.set(
            listOf(dependencyDir1, dependencyDir2)
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("GradleVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource()
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple toml files in different folders for gradle`() {
        // Given
        project.buildDir = File(buildDir, "build")
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        val firstRequirements = File(dependencyDir1, "dependencies.toml")
        firstRequirements.createNewFile().also {
            firstRequirements.writeText(loadResource("/gradle/versions.toml"))
        }

        val secondRequirements = File(dependencyDir2, "dependencies.toml")
        secondRequirements.createNewFile().also {
            secondRequirements.writeText(loadResource("/gradle/additional.versions.toml"))
        }

        val expected = loadResource("/task/MultiGradle.kt")

        // When
        task.packageName.set("some.name.test")
        task.gradleDirectory.set(
            listOf(dependencyDir1, dependencyDir2)
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("GradleVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource()
        )
    }
}
