/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.local.reader

import java.io.File
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class NodeReaderSpec {
    @TempDir
    private lateinit var fileDir: File
    private lateinit var file: File

    @BeforeEach
    fun setup() {
        file = File(fileDir, "file")
    }

    @Test
    fun `Given extractVersions is called it adds empty Map if no productionDependencies are given`() {
        // Given
        val content = javaClass.getResource("/node/Nothing.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.production,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it adds map of ProductionDependencies if given`() {
        // Given
        val content = javaClass.getResource("/node/All.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.production,
            expected = mapOf(
                "eslint" to "^5.9.0",
                "eslint-config-standard" to "^12.0.0",
            ),
        )
    }

    @Test
    fun `Given extractVersions is called it adds empty Map if no devDependencies are given`() {
        // Given
        val content = javaClass.getResource("/node/Nothing.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.development,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it adds map of DevelopmentDependencies if given`() {
        // Given
        val content = javaClass.getResource("/node/All.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.development,
            expected = mapOf(
                "babel-preset-env" to "1.7.0",
                "babel-register" to "^6.26.0",
            ),
        )
    }

    @Test
    fun `Given extractVersions is called it adds empty Map if no peerDependencies are given`() {
        // Given
        val content = javaClass.getResource("/node/Nothing.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.peer,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it adds map of PeerDependencies if given`() {
        // Given
        val content = javaClass.getResource("/node/All.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.peer,
            expected = mapOf(
                "mocha" to "^5.2.0",
                "mocha-eslint" to "^4.1.0",
            ),
        )
    }

    @Test
    fun `Given extractVersions is called it adds empty Map if no OptionalDependencies are given`() {
        // Given
        val content = javaClass.getResource("/node/Nothing.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.optional,
            expected = emptyMap(),
        )
    }

    @Test
    fun `Given extractVersions is called it adds map of OptionalDependencies if given`() {
        // Given
        val content = javaClass.getResource("/node/All.json")!!.readText()
        file.createNewFile().also {
            file.writeText(content)
        }

        // When
        val versions = DependencyReader.getNodeReader(file).extractVersions()

        // Then
        assertEquals(
            actual = versions.optional,
            expected = mapOf(
                "webpack" to "^4.26.1",
                "webpack-cli" to "^3.1.2",
            ),
        )
    }
}
