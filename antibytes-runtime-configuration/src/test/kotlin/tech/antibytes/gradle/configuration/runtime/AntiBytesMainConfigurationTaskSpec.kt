/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.runtime

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AntiBytesMainConfigurationTaskSpec {
    @TempDir
    private lateinit var buildDir: File
    private lateinit var file: File
    private lateinit var project: Project
    private val fixtureRoot = "/generated"

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.buildDir = buildDir
    }

    private fun loadResource(path: String): String {
        return AntiBytesMainConfigurationTaskSpec::class.java.getResource(fixtureRoot + path).readText()
    }

    private fun String.normalizeSource(): String {
        return this.replace(Regex("[\t ]+"), "")
    }

    @Test
    fun `It fulfils DefaultPlugin`() {
        val task: Any = project.tasks.create("sut", AntiBytesMainConfigurationTask::class.java) {}

        assert(task is DefaultTask)
    }

    @Test
    fun `It fulfils RuntimeConfigurationTask`() {
        val task: Any = project.tasks.create("sut", AntiBytesMainConfigurationTask::class.java) {}

        assert(task is RuntimeConfigurationContract.RuntimeConfigurationTask)
    }

    @Test
    fun `Given the task is executed it fails if no packageName is set`() {
        // Given
        val task: AntiBytesMainConfigurationTask = project.tasks.create("sut", AntiBytesMainConfigurationTask::class.java) {}

        // Then
        assertFailsWith<StopExecutionException> {
            // When
            task.generate()
        }
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesMainConfigurationTask = project.tasks.create("sut", AntiBytesMainConfigurationTask::class.java) {}
        val expected = loadResource("/MainConfigEmptyExpected.kt")

        // When
        task.packageName.set(packageName)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("MainConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource()
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/main/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main with StringFields`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesMainConfigurationTask = project.tasks.create("sut", AntiBytesMainConfigurationTask::class.java) {}
        val expected = loadResource("/MainConfigStringExpected.kt")

        // When
        task.packageName.set(packageName)
        task.stringFields.set(
            mapOf(
                "test" to "test1",
                "test1" to "test2"
            )
        )

        task.generate()

        // Then
        var fileValue = ""
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("MainConfig.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource()
        )
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main with IntFields`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesMainConfigurationTask = project.tasks.create("sut", AntiBytesMainConfigurationTask::class.java) {}
        val expected = loadResource("/MainConfigIntExpected.kt")

        // When
        task.packageName.set(packageName)
        task.integerFields.set(
            mapOf(
                "test" to 23,
                "test1" to 42
            )
        )

        task.generate()

        // Then
        var fileValue = ""
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("MainConfig.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource()
        )
    }
}
