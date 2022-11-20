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

class GradleReaderSpec {
    @TempDir
    private lateinit var fileDir: File
    private lateinit var file: File

    @BeforeEach
    fun setup() {
        file = File(fileDir, "file")
    }

    @Test
    fun `Given extractVersions is called it adds empty Map if no versions is set`() {
        // Given
        val content = javaClass.getResource("/gradle/empty.toml")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getGradleReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it adds empty Map if version are set but they are empty`() {
        // Given
        val content = javaClass.getResource("/gradle/emptyVersions.toml")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getGradleReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it returns a Map with versions`() {
        // Given
        val content = javaClass.getResource("/gradle/versions.toml")!!.readText()
        file.createNewFile().also {
            println(content)
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getGradleReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions,
            expected = mapOf(
                "kotlin" to "1.7.10",
                "gradle" to "7.5.1",
                "toml4j" to "0.7.2",
                "gson" to "2.10",
            ),
        )
    }
}
