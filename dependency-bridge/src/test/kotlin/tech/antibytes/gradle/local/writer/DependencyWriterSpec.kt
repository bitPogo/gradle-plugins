/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local.writer

import com.appmattus.kotlinfixture.kotlinFixture
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import tech.antibytes.gradle.local.DependencyVersionContract
import tech.antibytes.gradle.local.DependencyVersionContract.NodeDependencies

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
    fun `Given writePythonDependencies is called it ignores empty python dependencies`() {
        // Given
        val packageName = "com.test.python"

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writePythonDependencies(emptyMap())

        // Then
        var hasNodeDependency = false
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("PythonVersions.kt")) {
                hasNodeDependency = true
            }
        }

        assertFalse(hasNodeDependency)
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

    @Test
    fun `Given writeGradleDependencies is called it ignores empty gradle dependencies`() {
        // Given
        val packageName = "com.test.gradle"

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeGradleDependencies(emptyMap())

        // Then
        var hasNodeDependency = false
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("GradleVersions.kt")) {
                hasNodeDependency = true
            }
        }

        assertFalse(hasNodeDependency)
    }

    @Test
    fun `Given writeGradleDependencies is called it writes simple named key-value pairs`() {
        // Given
        val versions = mapOf(
            "test" to "1",
            "testa" to "2",
            "testb" to "3",
        )
        val packageName = "com.test.gradle"
        val expected = loadResource("/SimpleGradle.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeGradleDependencies(versions)

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("GradleVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeGradleDependencies is called it writes complex named key-value pairs`() {
        // Given
        val versions = mapOf(
            "test-with-strange-name" to "1",
            "test-with-strange-name-a" to "2",
            "test-with-strange-name-b" to "3",
        )
        val packageName = "com.test.gradle"
        val expected = loadResource("/ComplexGradle.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeGradleDependencies(versions)

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("GradleVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it ignores empty production dependencies`() {
        // Given
        val packageName = "com.test.gradle"

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var hasNodeDependency = false
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeProductionVersions.kt")) {
                hasNodeDependency = true
            }
        }

        assertFalse(hasNodeDependency)
    }

    @Test
    fun `Given writeNodeDependencies is called it writes simple named key-value pairs for production dependencies`() {
        // Given
        val versions = mapOf(
            "test" to "1",
            "testa" to "2",
            "testb" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/SimpleNodeProduction.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = versions,
                development = emptyMap(),
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeProductionVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it writes complex named key-value pairs for production dependencies`() {
        // Given
        val versions = mapOf(
            "test-with-strange-name" to "1",
            "test-with-strange-name-a" to "2",
            "test-with-strange-name-b" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/ComplexNodeProduction.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = versions,
                development = emptyMap(),
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeProductionVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it ignores empty development dependencies`() {
        // Given
        val packageName = "com.test.gradle"

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var hasNodeDependency = false
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeDevelopmentVersions.kt")) {
                hasNodeDependency = true
            }
        }

        assertFalse(hasNodeDependency)
    }

    @Test
    fun `Given writeNodeDependencies is called it writes simple named key-value pairs for development dependencies`() {
        // Given
        val versions = mapOf(
            "test" to "1",
            "testa" to "2",
            "testb" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/SimpleNodeDevelopment.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = versions,
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeDevelopmentVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it writes complex named key-value pairs for development dependencies`() {
        // Given
        val versions = mapOf(
            "test-with-strange-name" to "1",
            "test-with-strange-name-a" to "2",
            "test-with-strange-name-b" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/ComplexNodeDevelopment.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = versions,
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeDevelopmentVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it ignores empty peer dependencies`() {
        // Given
        val packageName = "com.test.gradle"

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var hasNodeDependency = false
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodePeerVersions.kt")) {
                hasNodeDependency = true
            }
        }

        assertFalse(hasNodeDependency)
    }

    @Test
    fun `Given writeNodeDependencies is called it writes simple named key-value pairs for peer dependencies`() {
        // Given
        val versions = mapOf(
            "test" to "1",
            "testa" to "2",
            "testb" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/SimpleNodePeer.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = versions,
                optional = emptyMap(),
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodePeerVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it writes complex named key-value pairs for peer dependencies`() {
        // Given
        val versions = mapOf(
            "test-with-strange-name" to "1",
            "test-with-strange-name-a" to "2",
            "test-with-strange-name-b" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/ComplexNodePeer.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = versions,
                optional = emptyMap(),
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodePeerVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it ignores empty optional dependencies`() {
        // Given
        val packageName = "com.test.gradle"

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = emptyMap(),
                optional = emptyMap(),
            ),
        )

        // Then
        var hasNodeDependency = false
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeOptionalVersions.kt")) {
                hasNodeDependency = true
            }
        }

        assertFalse(hasNodeDependency)
    }

    @Test
    fun `Given writeNodeDependencies is called it writes simple named key-value pairs for optional dependencies`() {
        // Given
        val versions = mapOf(
            "test" to "1",
            "testa" to "2",
            "testb" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/SimpleNodeOptional.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = emptyMap(),
                optional = versions,
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeOptionalVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }

    @Test
    fun `Given writeNodeDependencies is called it writes complex named key-value pairs for optional dependencies`() {
        // Given
        val versions = mapOf(
            "test-with-strange-name" to "1",
            "test-with-strange-name-a" to "2",
            "test-with-strange-name-b" to "3",
        )
        val packageName = "com.test.node"
        val expected = loadResource("/ComplexNodeOptional.kt")

        // When
        DependencyWriter(
            packageName = packageName,
            outputDirectory = dir,
        ).writeNodeDependencies(
            NodeDependencies(
                production = emptyMap(),
                development = emptyMap(),
                peer = emptyMap(),
                optional = versions,
            ),
        )

        // Then
        var fileValue = ""
        dir.walkBottomUp().toList().forEach { file ->
            if (file.absolutePath.endsWith("NodeOptionalVersions.kt")) {
                fileValue = file.readText()
            }
        }

        assertEquals(
            actual = fileValue.normalizeSource(),
            expected = expected.normalizeSource(),
        )
    }
}
