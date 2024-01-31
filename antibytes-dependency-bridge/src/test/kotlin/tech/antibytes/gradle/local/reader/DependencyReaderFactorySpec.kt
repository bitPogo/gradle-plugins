/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.local.reader

import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import tech.antibytes.gradle.local.DependencyVersionContract.Reader
import tech.antibytes.gradle.local.DependencyVersionContract.ReaderFactory

class DependencyReaderFactorySpec {
    @TempDir
    private lateinit var fileDir: File
    private lateinit var file: File

    @BeforeEach
    fun setup() {
        file = File(fileDir, "file")
    }

    @Test
    fun `It fulfils DependencyReaderFactory`() {
        val factory = DependencyReader as Any
        assertTrue(factory is ReaderFactory)
    }

    @Test
    fun `Given getPythonReader is called it fails if the File does not exists`() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getPythonReader(file)
        }

        assertEquals(
            expected = "The given file does not exists.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getPythonReader is called it fails if the File is not a File`() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getPythonReader(fileDir)
        }

        assertEquals(
            expected = "The given file is not a file.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getPythonReader is called it fails if the File is readable`() {
        // Given
        file.createNewFile()
        file.setReadable(false)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getPythonReader(file)
        }

        assertEquals(
            expected = "The given file is not readable.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getPythonReader is returns a python reader`() {
        // Given
        file.createNewFile()

        // When
        val reader: Any = DependencyReader.getPythonReader(file)

        // Then
        assertTrue(
            reader is Reader<*>,
        )
    }

    @Test
    fun `Given getNodeReader is called it fails if the File does not exists`() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getNodeReader(file)
        }

        assertEquals(
            expected = "The given file does not exists.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getNodeReader is called it fails if the File is not a File`() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getNodeReader(fileDir)
        }

        assertEquals(
            expected = "The given file is not a file.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getNodeReader is called it fails if the File is readable`() {
        // Given
        file.createNewFile()
        file.setReadable(false)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getNodeReader(file)
        }

        assertEquals(
            expected = "The given file is not readable.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getNodeReader is returns a Node reader`() {
        // Given
        file.createNewFile()

        // When
        val reader: Any = DependencyReader.getNodeReader(file)

        // Then
        assertTrue(
            reader is Reader<*>,
        )
    }

    @Test
    fun `Given getGradleReader is called it fails if the File does not exists`() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getGradleReader(file)
        }

        assertEquals(
            expected = "The given file does not exists.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getGradleReader is called it fails if the File is not a File`() {
        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getGradleReader(fileDir)
        }

        assertEquals(
            expected = "The given file is not a file.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getGradleReader is called it fails if the File is readable`() {
        // Given
        file.createNewFile()
        file.setReadable(false)

        // Then
        val error = assertFailsWith<IllegalArgumentException> {
            // When
            DependencyReader.getGradleReader(file)
        }

        assertEquals(
            expected = "The given file is not readable.",
            actual = error.message,
        )
    }

    @Test
    fun `Given getGradleReader is returns a Gradle reader`() {
        // Given
        file.createNewFile()

        // When
        val reader: Any = DependencyReader.getGradleReader(file)

        // Then
        assertTrue(
            reader is Reader<*>,
        )
    }
}
