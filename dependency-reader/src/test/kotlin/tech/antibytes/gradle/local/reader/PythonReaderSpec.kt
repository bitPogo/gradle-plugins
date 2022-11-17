/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local.reader

import java.io.File
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class PythonReaderSpec {
    @TempDir
    private lateinit var fileDir: File
    private lateinit var file: File

    @BeforeEach
    fun setup() {
        file = File(fileDir, "file")
    }

    @Test
    fun `Given extractVersions is called it ignores Lines which have no separator`() {
        // Given
        val content = javaClass.getResource("/python/NoSeparator.txt")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getPythonReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it ignores Lines which have multiple separator`() {
        // Given
        val content = javaClass.getResource("/python/MultipleSeparator.txt")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getPythonReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it returns a Map of Version by their package name`() {
        // Given
        val content = javaClass.getResource("/python/Regular.txt")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getPythonReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions,
            expected = mapOf(
                "pymdown-extensions" to "9.5",
                "mkdocs-redirects" to "1.1.0",
                "pygments" to "2.13.0",
            ),
        )
    }
}
