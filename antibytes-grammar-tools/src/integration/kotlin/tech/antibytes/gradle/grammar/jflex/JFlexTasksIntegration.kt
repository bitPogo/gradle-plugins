/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.grammar.jflex

import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class JFlexTasksIntegration {
    @TempDir
    private lateinit var testProjectDir: File
    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var outputDir: File

    @BeforeEach
    fun setUp() {
        settingsFile = File(testProjectDir, "settings.gradle.kts").also { it.createNewFile() }

        buildFile = File(testProjectDir, "build.gradle.kts").also { it.createNewFile() }

        outputDir = File(testProjectDir, "output").also { it.mkdir() }

        val settingsFileContent = JFlexTasksIntegration::class.java.getResource(
            "/sample.settings.gradle.kts.txt",
        )?.readText()

        settingsFile.writeText(settingsFileContent!!)
    }

    @Test
    fun `Given a JFlex task is executed it generates a Java file`() {
        // Given
        val flexFile = JFlexTasksIntegration::class.java.getResource("/Simple.flex")?.path!!
        val buildFileContent = JFlexTasksIntegration::class.java.getResource("/sample.build.jflex.gradle.kts.txt")?.readText()!!
        val expected = JFlexTasksIntegration::class.java.getResource("/LexerClassName.java.txt")?.readText()!!

        buildFile.writeText(
            buildFileContent.replace(
                "\$FLEX_FILE",
                flexFile,
            ).replace(
                "\$OUTPUT_DIR",
                outputDir.absolutePath,
            ),
        )

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("jflex")
            .withPluginClasspath()
            .build()

        val generatedFile = File("${outputDir.absolutePath}/LexerClassName.java")

        // Then
        assertEquals(
            actual = result.task(":jflex")?.outcome,
            expected = SUCCESS,
        )
        assertTrue(generatedFile.exists())
        assertEquals(
            actual = generatedFile.readText()
                .replace("// source:[a-zA-Z0-9/ \\-.]+\n".toRegex(), "")
                .replace("[ ]+\n".toRegex(), "\n"),
            expected = expected,
        )
    }
}
