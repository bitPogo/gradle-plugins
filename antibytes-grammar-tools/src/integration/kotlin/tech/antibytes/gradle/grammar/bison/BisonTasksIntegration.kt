/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar.bison

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.BeforeEach
import org.junit.Ignore
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BisonTasksIntegration {
    @JvmField
    @Rule
    val testDir: TemporaryFolder = TemporaryFolder()
    private lateinit var testProjectDir: File
    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var outputFile: File
    private val bisonExec = BisonTasksIntegration::class.java.getResource(
        "/bisonExec"
    )?.readText()!!.trim()

    @BeforeEach
    fun setUp() {
        testProjectDir = testDir.newFolder()

        settingsFile = File(testProjectDir, "settings.gradle.kts").also { it.createNewFile() }

        buildFile = File(testProjectDir, "build.gradle.kts").also { it.createNewFile() }

        outputFile = File(testProjectDir, "output.java")

        val settingsFileContent = BisonTasksIntegration::class.java.getResource(
            "/sample.settings.gradle.kts.txt"
        )?.readText()

        settingsFile.writeText(settingsFileContent!!)
    }

    @Test
    @Ignore // TODO Something is currently wrong on the CI
    fun `Given a Bison task is executed it generates a Java file`() {
        // Given
        val grammarFile = BisonTasksIntegration::class.java.getResource("/Simple.y")?.path!!
        val buildFileContent = BisonTasksIntegration::class.java.getResource("/sample.build.bison.gradle.kts.txt")?.readText()!!
        val expected = BisonTasksIntegration::class.java.getResource("/Calc.java.txt")?.readText()!!.trim()

        buildFile.writeText(
            buildFileContent.replace(
                "\$BISON_EXEC",
                bisonExec
            ).replace(
                "\$GRAMMAR_FILE",
                grammarFile
            ).replace(
                "\$OUTPUT_FILE",
                outputFile.absolutePath
            )
        )

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("bison")
            .withPluginClasspath()
            .build()

        // Then
        assertEquals(
            actual = result.task(":bison")?.outcome,
            expected = SUCCESS
        )
        assertTrue(outputFile.exists())

        val actual = outputFile
            .readText()
            .replace("[a-zA-z0-9/\\-.]+(Simple|output).(java|y)".toRegex(), "")
            .trim()

        assertEquals(
            actual = actual,
            expected = expected
        )
    }
}
