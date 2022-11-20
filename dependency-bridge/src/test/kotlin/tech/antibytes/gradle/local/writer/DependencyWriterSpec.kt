/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local.writer

import com.appmattus.kotlinfixture.kotlinFixture
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import tech.antibytes.gradle.local.DependencyVersionContract

class DependencyWriterSpec {
    @TempDir
    private lateinit var dir: File
    private val fixture = kotlinFixture()
    private val fixtureRoot = "/generated"

    private fun loadResource(path: String): String {
        return this::class.java.getResource(fixtureRoot + path)!!.readText()
    }

    private fun String.normalizeSource(): String {
        return this.replace(Regex("[\t ]+"), "").replace(Regex("[\n]+"), "\n")
    }

    @Test
    fun `It fulfils DependencyWriter`() {
        // When
        val writer: Any = DependencyWriter(fixture(), dir)

        // Then
        assertTrue(writer is DependencyVersionContract.Writer)
    }

    @Test
    fun `Given writePythonDependencies is called it writes simple named key-value pairs`() {
        // Given
        val versions = mapOf(
            "test" to "1",
            "testa" to "2",
            "testb" to "3",
        )
        val packageName = "com.test.python"
        val expected = loadResource("/SimplePython.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writePythonDependencies(versions)

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("PythonVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writePythonDependencies is called it writes complex named key-value pairs`() {
        // Given
        val versions = mapOf(
            "test-with-strange-name" to "1",
            "test-with-strange-name-a" to "2",
            "test-with-strange-name-b" to "3",
        )
        val packageName = "com.test.python"
        val expected = loadResource("/ComplexPython.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writePythonDependencies(versions)

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("PythonVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }
}
