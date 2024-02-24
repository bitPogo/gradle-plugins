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

class AntiBytesAcceptanceTestConfigurationTaskSpec {
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
        return AntiBytesAcceptanceTestConfigurationTaskSpec::class.java.getResource(fixtureRoot + path).readText()
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
        val task: Any = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}

        assert(task is DefaultTask)
    }

    @Test
    fun `It fulfils RuntimeConfigurationTask`() {
        val task: Any = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}

        assert(task is RuntimeConfigurationContract.RuntimeConfigurationTask)
    }

    @Test
    fun `Given the task is executed it fails if no packageName is set`() {
        // Given
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}

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
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}

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
    fun `Given the task is executed it generates a OutputFile in acceptance`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        val expected = loadResource("/AcceptanceTestConfigEmptyExpected.kt")

        // When
        task.packageName.set(packageName)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/acceptanceTest/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in acceptance while having a sourceSet`() {
        // Given
        val packageName = "test.config"
        val sourceSet = "somewhere"
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        val expected = loadResource("/AcceptanceTestConfigEmptyExpected.kt")

        // When
        task.packageName.set(packageName)
        task.sourceSetPrefix.set(sourceSet)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/acceptanceTest/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in commonAcceptance`() {
        // Given
        val packageName = "test.config"
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")

        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        val expected = loadResource("/AcceptanceTestConfigEmptyExpected.kt")

        // When
        task.packageName.set(packageName)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/commonAcceptanceTest/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in custom sourceSet`() {
        // Given
        val packageName = "test.config"
        val sourceSet = "somewhere"
        val expected = loadResource("/AcceptanceTestConfigEmptyExpected.kt")

        // When
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")

        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        task.packageName.set(packageName)
        task.sourceSetPrefix.set(sourceSet)
        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/${sourceSet}AcceptanceTest/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile with the given prefix for the File`() {
        // Given
        val packageName = "test.config"
        val prefix = "somewhere"
        val expected = loadResource("/AcceptanceTestConfigEmptyExpected.kt")

        // When
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        task.configurationFilePrefix.set(prefix)
        task.packageName.set(packageName)

        task.generate()

        // Then
        var fileValue = ""
        var pointer: File? = null
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("${File.separator}${prefix.capitalize()}AcceptanceTestConfig.kt")) {
                fileValue = file.readText()
                pointer = file
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )

        assertTrue(pointer?.absolutePath?.contains("generated/antibytes/acceptanceTest/kotlin") ?: false)
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main with StringFields`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        val expected = loadResource("/AcceptanceTestConfigStringExpected.kt")

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
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
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
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        val expected = loadResource("/AcceptanceTestConfigIntExpected.kt")

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
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given the task is executed it generates a OutputFile in main with LongFields`() {
        // Given
        val packageName = "test.config"
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        val expected = loadResource("/AcceptanceTestConfigLongExpected.kt")

        // When
        task.packageName.set(packageName)
        task.longFields.set(
            mapOf(
                "test" to 23,
                "test1" to 42,
            ),
        )

        task.generate()

        // Then
        var fileValue = ""
        buildDir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
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
        val task: AntiBytesAcceptanceTestConfigurationTask = project.tasks.create("sut", AntiBytesAcceptanceTestConfigurationTask::class.java) {}
        val expected = loadResource("/AcceptanceTestConfigBooleanExpected.kt")

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
            if (file.absolutePath.endsWith("AcceptanceTestConfig.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            fileValue.normalizeSource(),
            expected.normalizeSource(),
        )
    }
}
