/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.grammar

import java.io.File
import kotlin.test.assertEquals
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class PostConverterIntegration {
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

        val settingsFileContent = PostConverterIntegration::class.java.getResource(
            "/sample.settings.gradle.kts.txt",
        )?.readText()

        settingsFile.writeText(settingsFileContent!!)
    }

    @Test
    fun `Given a PostConverter task is executed it cleans up a file`() {
        // Given
        val file = File(testProjectDir, "dirtyFile.txt")
        file.writeText("abcTestabc")

        val buildFileContent = PostConverterIntegration::class.java.getResource("/sample.build.postprocess.gradle.kts.txt")?.readText()!!

        buildFile.writeText(
            buildFileContent.replace(
                "\$FLEX_CONVERTED_FILE",
                file.absolutePath,
            ).replace(
                "\$FLEX_CONVERTED_REPLACEMENT_PATTERN",
                "[a-c]+",
            ).replace(
                "\$FLEX_CONVERTED_REPLACEMENT_VALUE",
                "x ",
            ),
        )

        // When
        val result = GradleRunner.create()
            .withProjectDir(testProjectDir)
            .withArguments("postProcessJFlex")
            .withPluginClasspath()
            .build()

        assertEquals(
            actual = result.task(":postProcessJFlex")?.outcome,
            expected = TaskOutcome.SUCCESS,
        )

        assertEquals(
            actual = file.readText(),
            expected = "x Testx ",
        )
    }
}
