/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.jflex

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JFlexTaskIntegration {
    @JvmField
    @Rule
    val testDir: TemporaryFolder = TemporaryFolder()
    private lateinit var testProjectDir: File
    private lateinit var settingsFile: File
    private lateinit var buildFile: File
    private lateinit var outputDir: File

    @Before
    fun setUp() {
        testProjectDir = testDir.newFolder()

        settingsFile = File(testProjectDir, "settings.gradle.kts").also { it.createNewFile() }

        buildFile = File(testProjectDir, "build.gradle.kts").also { it.createNewFile() }

        outputDir = File(testProjectDir, "output").also { it.mkdir() }

        val settingsFileContent = JFlexTaskIntegration::class.java.getResource(
            "/sample.settings.gradle.kts.txt"
        )?.readText()

        settingsFile.writeText(settingsFileContent!!)
    }

    @Test
    fun `Given a JFlex task is executed it generates a Java file`() {
        // Given
        val flexFile = JFlexTaskIntegration::class.java.getResource("/Simple.flex")?.path!!
        val buildFileContent = JFlexTaskIntegration::class.java.getResource("/sample.build.gradle.kts.txt")?.readText()!!
        val expected = JFlexTaskIntegration::class.java.getResource("/LexerClassName.java.txt")?.readText()!!

        buildFile.writeText(
            buildFileContent.replace(
                "\$FLEX_FILE",
                flexFile
            ).replace(
                "\$OUTPUT_DIR",
                outputDir.absolutePath
            )
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
            expected = SUCCESS
        )
        assertTrue(generatedFile.exists())
        assertEquals(
            actual = generatedFile.readText(),
            expected = expected
        )
    }
}
