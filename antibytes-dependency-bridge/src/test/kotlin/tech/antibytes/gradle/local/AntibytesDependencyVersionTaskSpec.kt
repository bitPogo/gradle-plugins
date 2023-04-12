/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local

import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import tech.antibytes.gradle.local.DependencyVersionContract.DependencyVersionTask

class AntibytesDependencyVersionTaskSpec {
    @TempDir
    private lateinit var projectDir: File
    private lateinit var buildDir: File

    @TempDir
    private lateinit var root: File

    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder()
            .withProjectDir(projectDir)
            .build()
        buildDir = File(projectDir, "build")
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
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        // When
        task.packageName.set("packageName")
        task.generate()

        // Then
        assertTrue {
            project.layout.buildDirectory.get().asFile.absolutePath.endsWith(
                buildDir.absolutePath,
            )
        }

        assertTrue(buildDir.exists())
    }

    @Test
    fun `Given the task is executed it ignores if there are no files`() {
        // Given
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
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val singleRequirements = File(root, "requirements.txt")
        singleRequirements.createNewFile().also {
            singleRequirements.writeText(loadResource("/python/Regular.txt"))
        }

        val expected = loadResource("/task/SinglePython.kt")

        // When
        task.packageName.set("some.name.test")
        task.pythonDirectory.set(
            listOf(root),
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
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple requirements files for python`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val dependencyDir1 = File(root, "A")
        dependencyDir1.mkdir()

        val dependencyDir2 = File(root, "B")
        dependencyDir2.mkdir()

        val firstDependencies = File(dependencyDir1, "requirements.txt")
        firstDependencies.createNewFile().also {
            firstDependencies.writeText(loadResource("/python/Regular.txt"))
        }

        val secondDependencies = File(dependencyDir2, "requirements.txt")
        secondDependencies.createNewFile().also {
            secondDependencies.writeText(loadResource("/python/Additional.txt"))
        }

        val expected = loadResource("/task/MultiPython.kt")

        // When
        task.packageName.set("some.name.test")
        task.pythonDirectory.set(
            listOf(dependencyDir1, dependencyDir2),
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
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a single toml file for gradle`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val singleRequirements = File(root, "dependencies.toml")
        singleRequirements.createNewFile().also {
            singleRequirements.writeText(loadResource("/gradle/versions.toml"))
        }

        val expected = loadResource("/task/SingleGradle.kt")

        // When
        task.packageName.set("some.name.test")
        task.gradleDirectory.set(
            listOf(root),
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
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple toml files in the same folders for gradle`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}

        val firstDependencies = File(root, "base.toml")
        firstDependencies.createNewFile().also {
            firstDependencies.writeText(loadResource("/gradle/versions.toml"))
        }

        val secondDependencies = File(root, "extended.toml")
        secondDependencies.createNewFile().also {
            secondDependencies.writeText(loadResource("/gradle/additional.versions.toml"))
        }

        val expected = loadResource("/task/MultiGradle.kt")

        // When
        task.packageName.set("some.name.test")
        task.gradleDirectory.set(
            listOf(root),
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
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple toml files in different folders for gradle`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val dependencyDir1 = File(root, "A")
        dependencyDir1.mkdir()

        val dependencyDir2 = File(root, "B")
        dependencyDir2.mkdir()

        val firstDependencies = File(dependencyDir1, "dependencies.toml")
        firstDependencies.createNewFile().also {
            firstDependencies.writeText(loadResource("/gradle/versions.toml"))
        }

        val secondDependencies = File(dependencyDir2, "dependencies.toml")
        secondDependencies.createNewFile().also {
            secondDependencies.writeText(loadResource("/gradle/additional.versions.toml"))
        }

        val expected = loadResource("/task/MultiGradle.kt")

        // When
        task.packageName.set("some.name.test")
        task.gradleDirectory.set(
            listOf(dependencyDir1, dependencyDir2),
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
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a single package json file for node`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val singleRequirements = File(root, "package.json")
        singleRequirements.createNewFile().also {
            singleRequirements.writeText(loadResource("/node/All.json"))
        }

        val expected = loadResource("/task/SingleNodeProduction.kt")

        // When
        task.packageName.set("some.name.test")
        task.nodeDirectory.set(
            listOf(root),
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeProductionVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple package json for node while merging the production dependencies`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val dependencyDir1 = File(root, "A")
        dependencyDir1.mkdir()

        val dependencyDir2 = File(root, "B")
        dependencyDir2.mkdir()

        val firstDependencies = File(dependencyDir1, "package.json")
        firstDependencies.createNewFile().also {
            firstDependencies.writeText(loadResource("/node/All.json"))
        }

        val secondDependencies = File(dependencyDir2, "package.json")
        secondDependencies.createNewFile().also {
            secondDependencies.writeText(loadResource("/node/Additional.json"))
        }

        val expected = loadResource("/task/MultiNodeProduction.kt")

        // When
        task.packageName.set("some.name.test")
        task.nodeDirectory.set(
            listOf(dependencyDir1, dependencyDir2),
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeProductionVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple package json for node while merging the development dependencies`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val dependencyDir1 = File(root, "A")
        dependencyDir1.mkdir()

        val dependencyDir2 = File(root, "B")
        dependencyDir2.mkdir()

        val firstDependencies = File(dependencyDir1, "package.json")
        firstDependencies.createNewFile().also {
            firstDependencies.writeText(loadResource("/node/All.json"))
        }

        val secondDependencies = File(dependencyDir2, "package.json")
        secondDependencies.createNewFile().also {
            secondDependencies.writeText(loadResource("/node/Additional.json"))
        }

        val expected = loadResource("/task/MultiNodeDevelopment.kt")

        // When
        task.packageName.set("some.name.test")
        task.nodeDirectory.set(
            listOf(dependencyDir1, dependencyDir2),
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeDevelopmentVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple package json for node while merging the peer dependencies`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val dependencyDir1 = File(root, "A")
        dependencyDir1.mkdir()

        val dependencyDir2 = File(root, "B")
        dependencyDir2.mkdir()

        val firstDependencies = File(dependencyDir1, "package.json")
        firstDependencies.createNewFile().also {
            firstDependencies.writeText(loadResource("/node/All.json"))
        }

        val secondDependencies = File(dependencyDir2, "package.json")
        secondDependencies.createNewFile().also {
            secondDependencies.writeText(loadResource("/node/Additional.json"))
        }

        val expected = loadResource("/task/MultiNodePeer.kt")

        // When
        task.packageName.set("some.name.test")
        task.nodeDirectory.set(
            listOf(dependencyDir1, dependencyDir2),
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodePeerVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it writes the content of a multiple package json for node while merging the optional dependencies`() {
        // Given
        val task = project.tasks.create("sut", AntibytesDependencyVersionTask::class.java) {}
        val dependencyDir1 = File(root, "A")
        dependencyDir1.mkdir()

        val dependencyDir2 = File(root, "B")
        dependencyDir2.mkdir()

        val firstDependencies = File(dependencyDir1, "package.json")
        firstDependencies.createNewFile().also {
            firstDependencies.writeText(loadResource("/node/All.json"))
        }

        val secondDependencies = File(dependencyDir2, "package.json")
        secondDependencies.createNewFile().also {
            secondDependencies.writeText(loadResource("/node/Additional.json"))
        }

        val expected = loadResource("/task/MultiNodeOptional.kt")

        // When
        task.packageName.set("some.name.test")
        task.nodeDirectory.set(
            listOf(dependencyDir1, dependencyDir2),
        )
        task.generate()

        // Then
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeOptionalVersions.kt")) {
                pointer = file
            }
        }

        assertNotNull(pointer)
        assertEquals(
            actual = pointer!!.readText().normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }
}
