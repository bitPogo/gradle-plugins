/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.runtime

import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.StopExecutionException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import tech.antibytes.gradle.util.capitalize

class AntiBytesTestConfigurationTaskSpec {
    @TempDir
    private lateinit var buildDir: File
    private lateinit var project: Project
    private val fixtureRoot = "/generated"

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder().build()
        project.buildDir = buildDir
    }

    private fun loadResource(path: String): String {
        return AntiBytesTestConfigurationTaskSpec::class.java.getResource(fixtureRoot + path).readText()
    }

    private fun String.normalizeSource(): String {
        return this
            .replace(Regex("^/\\*\n", RegexOption.MULTILINE), "")
            .replace(Regex("^ +\\*.*", RegexOption.MULTILINE), "")
            .replace(Regex("^\\*/", RegexOption.MULTILINE), "")
            .trimStart()
            .replace(Regex("[\t ]+"), "")
            .replace(Regex("[\n]+"), "\n")
    }

    @Test
    fun `It fulfils DefaultPlugin`() {
        val task: Any = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}

        assert(task is DefaultTask)
    }

    @Test
    fun `It fulfils RuntimeConfigurationTask`() {
        val task: Any = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}

        assert(task is RuntimeConfigurationContract.RuntimeConfigurationTask)
    }

    @Test
    fun `Given the task is executed it fails if no packageName is set`() {
        // Given
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}

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
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}

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
    fun `Given the task is executed it generates a OutputFile in test`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        val expected = loadResource("/TestConfigEmptyExpected.kt")

        // When
        task.packageName.set(packageName)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("TestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/test/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in test while having a custom prefix for SourceSets`() {
        // Given
        val packageName = "test.config"
        val sourceSet = "somewhere"
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        val expected = loadResource("/TestConfigEmptyExpected.kt")

        // When
        task.packageName.set(packageName)
        task.sourceSetPrefix.set(sourceSet)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("TestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/test/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile with the given prefix for the File`() {
        // Given
        val packageName = "test.config"
        val prefix = "somewhere"
        val expected = loadResource("/TestConfigEmptyExpected.kt")

        // When
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        task.configurationFilePrefix.set(prefix)
        task.packageName.set(packageName)

        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("${File.separator}${prefix.capitalize()}TestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/test/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in commonTest`() {
        // Given
        val packageName = "test.config"
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")

        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        val expected = loadResource("/TestConfigEmptyExpected.kt")

        // When
        task.packageName.set(packageName)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("TestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/commonTest/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile with a custom sourceSet`() {
        // Given
        val packageName = "test.config"
        val sourceSet = "somewhere"
        val expected = loadResource("/TestConfigEmptyExpected.kt")

        // When
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")

        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        task.packageName.set(packageName)
        task.sourceSetPrefix.set(sourceSet)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("TestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/${sourceSet}Test/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main with StringFields`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        val expected = loadResource("/TestConfigStringExpected.kt")

        // When
        task.packageName.set(packageName)
        task.stringFields.set(
            mapOf(
                "test" to "test1",
                "test1" to "test2",
            ),
        )

        task.generate()

        // Then
        var fileValue = ""
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("TestConfig.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main with IntFields`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        val expected = loadResource("/TestConfigIntExpected.kt")

        // When
        task.packageName.set(packageName)
        task.integerFields.set(
            mapOf(
                "test" to 23,
                "test1" to 42,
            ),
        )

        task.generate()

        // Then
        var fileValue = ""
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("TestConfig.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main with BooleanFields`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesTestConfigurationTask = project.tasks.create("sut", AntiBytesTestConfigurationTask::class.java) {}
        val expected = loadResource("/TestConfigBooleanExpected.kt")

        // When
        task.packageName.set(packageName)
        task.booleanFields.set(
            mapOf(
                "test" to true,
                "test1" to false,
            ),
        )

        task.generate()

        // Then
        var fileValue = ""
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("TestConfig.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )
    }
}
