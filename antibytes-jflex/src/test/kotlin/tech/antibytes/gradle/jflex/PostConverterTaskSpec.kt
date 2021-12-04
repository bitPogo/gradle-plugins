/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.jflex

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PostConverterTaskSpec {
    @JvmField
    @Rule
    val testDir: TemporaryFolder = TemporaryFolder()
    private lateinit var file: File
    private lateinit var project: Project

    @Before
    fun setup() {
        project = ProjectBuilder.builder().build()

        file = testDir.newFile()
    }

    @Test
    fun `It fulfils PostConverterTask`() {
        val task: Any = project.tasks.create("sut", PostConverterTask::class.java) {}

        assertTrue(task is JFlexApiContract.PostConverterTask)
    }

    @Test
    fun `It has no default TargetFile`() {
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        assertFalse(task.targetFile.isPresent)
    }

    @Test
    fun `It has an empty List as default for replaceWithString`() {
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        assertEquals(
            actual = task.replaceWithString.get(),
            expected = emptyList<Pair<String, String>>()
        )
    }

    @Test
    fun `It has an empty List as default for replaceWithRegEx`() {
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        assertEquals(
            actual = task.replaceWithRegEx.get(),
            expected = emptyList<Pair<Regex, String>>()
        )
    }

    @Test
    fun `It has an empty List as default for deleteWithString`() {
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        assertEquals(
            actual = task.deleteWithString.get(),
            expected = emptyList<String>()
        )
    }

    @Test
    fun `It has an empty List as default for deleteWithRegEx`() {
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        assertEquals(
            actual = task.deleteWithRegEx.get(),
            expected = emptyList<Regex>()
        )
    }

    @Test
    fun `Given cleanUp is called, it fails if no target file was provided`() {
        mockkStatic(Logging::class)
        // Given
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(PostConverterTask::class.java) } returns logger

        // Then
        val error = assertFailsWith<PostConverterTaskError.TargetFileNotFound> {
            // When
            task.cleanUp()
        }

        val message = "There was no file provided to process."
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }

        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given cleanUp is called, it replaces with the provided string Replacements`() {
        // Given
        val replacements = listOf(
            "aaa" to "x"
        )

        val targetFile = project.file(file)
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val fileValue = "aaaTestaaa"
        file.writeText(fileValue)

        // When
        task.targetFile.set(targetFile)
        task.replaceWithString.set(replacements)
        task.cleanUp()

        // Then
        val actual = file.readText()

        assertEquals(
            actual = actual,
            expected = "xTestx"
        )
    }

    @Test
    fun `Given cleanUp is called, it replaces with the provided RegEx Replacements`() {
        // Given
        val replacements = listOf(
            "[ab12]+([Test]+)[ab12]+".toRegex() to "$1"
        )

        val targetFile = project.file(file)
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val fileValue = "a12Testb12"
        file.writeText(fileValue)

        // When
        task.targetFile.set(targetFile)
        task.replaceWithRegEx.set(replacements)
        task.cleanUp()

        // Then
        val actual = file.readText()

        assertEquals(
            actual = actual,
            expected = "Test"
        )
    }

    @Test
    fun `Given cleanUp is called, it replaces with the provided RegEx Replacements after String Replacements`() {
        // Given
        val stringReplacements = listOf(
            "Test" to "Noway"
        )
        val regexReplacements = listOf(
            "[ab12]+([Test]+)[ab12]+".toRegex() to "$1"
        )

        val targetFile = project.file(file)
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val fileValue = "a12Testb12"
        file.writeText(fileValue)

        // When
        task.targetFile.set(targetFile)
        task.replaceWithString.set(stringReplacements)
        task.replaceWithRegEx.set(regexReplacements)
        task.cleanUp()

        // Then
        val actual = file.readText()

        assertEquals(
            actual = actual,
            expected = "a12Nowayb12"
        )
    }

    @Test
    fun `Given cleanUp is called, it removes with the provided string Deletions`() {
        // Given
        val deletions = listOf(
            "aaa"
        )

        val targetFile = project.file(file)
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val fileValue = "aaaTestaaa"
        file.writeText(fileValue)

        // When
        task.targetFile.set(targetFile)
        task.deleteWithString.set(deletions)
        task.cleanUp()

        // Then
        val actual = file.readText()

        assertEquals(
            actual = actual,
            expected = "Test"
        )
    }

    @Test
    fun `Given cleanUp is called, it removes with the provided RegEx Deletions`() {
        // Given
        val deletions = listOf(
            "[a-zA-Z]".toRegex()
        )

        val targetFile = project.file(file)
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val fileValue = "a1a2aTe3st4a5a6a7"
        file.writeText(fileValue)

        // When
        task.targetFile.set(targetFile)
        task.deleteWithRegEx.set(deletions)
        task.cleanUp()

        // Then
        val actual = file.readText()

        assertEquals(
            actual = actual,
            expected = "1234567"
        )
    }

    @Test
    fun `Given cleanUp is called, it removes with the provided RegEx Deletions after String Deletion`() {
        // Given
        val stringDeletions = listOf(
            "a"
        )
        val regExDeletions = listOf(
            "a[bc]".toRegex()
        )

        val targetFile = project.file(file)
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val fileValue = "abcTestabc"
        file.writeText(fileValue)

        // When
        task.targetFile.set(targetFile)
        task.deleteWithString.set(stringDeletions)
        task.deleteWithRegEx.set(regExDeletions)
        task.cleanUp()

        // Then
        val actual = file.readText()

        assertEquals(
            actual = actual,
            expected = "bcTestbc"
        )
    }

    @Test
    fun `Given cleanUp is called, it run the provided String Deletion after RegEx Replacements`() {
        // Given
        val stringDeletions = listOf(
            "a"
        )
        val regExReplacements = listOf(
            "a[bc]+".toRegex() to "bb"
        )

        val targetFile = project.file(file)
        val task = project.tasks.create("sut", PostConverterTask::class.java) {}

        val fileValue = "abcTestabc"
        file.writeText(fileValue)

        // When
        task.targetFile.set(targetFile)
        task.deleteWithString.set(stringDeletions)
        task.replaceWithRegEx.set(regExReplacements)
        task.cleanUp()

        // Then
        val actual = file.readText()

        assertEquals(
            actual = actual,
            expected = "bbTestbb"
        )
    }
}
