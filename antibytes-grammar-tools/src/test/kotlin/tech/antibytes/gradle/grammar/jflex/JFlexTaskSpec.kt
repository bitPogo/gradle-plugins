/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar.jflex

import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.EqMatcher
import io.mockk.OfTypeMatcher
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic
import io.mockk.verify
import jflex.generator.LexGenerator
import jflex.logging.Out
import jflex.option.Options
import jflex.skeleton.Skeleton
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test
import tech.antibytes.gradle.grammar.JFlexTaskContract
import java.io.File
import java.nio.charset.Charset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JFlexTaskSpec {
    private lateinit var project: Project
    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        project = ProjectBuilder.builder().build()
    }

    @Test
    fun `It fulfils JFlexTask`() {
        val task: Any = project.tasks.create("sut", JFlexTask::class.java) {}

        assertTrue(task is JFlexTaskContract)
    }

    @Test
    fun `It has no default FlexFile`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.flexFile.isPresent)
    }

    @Test
    fun `It has no default OutputDirectory`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.outputDirectory.isPresent)
    }

    @Test
    fun `It has a UTF-8 as default encoding`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertEquals(
            actual = task.encoding.get(),
            expected = Charsets.UTF_8.name()
        )
    }

    @Test
    fun `It has no default CustomSkeletonFile`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.customSkeletonFile.isPresent)
    }

    @Test
    fun `It is not verbose on default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.verbose.get())
    }

    @Test
    fun `It does not show the Dump by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.showDump.get())
    }

    @Test
    fun `It does not show TimeStatistics by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.showTimeStatics.get())
    }

    @Test
    fun `It does not show Statistics by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.showStatistic.get())
    }

    @Test
    fun `It does not generate DotFiles by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.generateDotFile.get())
    }

    @Test
    fun `It does not backup by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.backup.get())
    }

    @Test
    fun `It does minimize by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertTrue(task.minimize.get())
    }

    @Test
    fun `It is not JLex compatible by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.useJLexCompatibilityMode.get())
    }

    @Test
    fun `It does not use legacy Dot by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.useLegacyDot.get())
    }

    @Test
    fun `It does not disable UnUsedWarnings by default`() {
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        assertFalse(task.disableUnusedWarning.get())
    }

    @Test
    fun `Given generate is called, it fails and logs the Error if no Flex file was given`() {
        mockkStatic(Logging::class)
        // Given
        val task = project.tasks.create("sut", JFlexTask::class.java) {}

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(JFlexTask::class.java) } returns logger

        // Then
        val error = assertFailsWith<JFlexTaskError.MissingFlexFileError> {
            // When
            task.generate()
        }

        val message = "There was no file provided for JFlex to process."
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }

        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given generate is called, it fails and logs the Error if no OutputDirectory was given`() {
        mockkStatic(Logging::class)

        // Given
        val flexFile = project.file("somewhere/something.l")

        val task = project.tasks.create("sut", JFlexTask::class.java) {}
        task.flexFile.set(flexFile)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(JFlexTask::class.java) } returns logger

        // Then
        val error = assertFailsWith<JFlexTaskError.MissingOutputDirectoryError> {
            // When
            task.generate()
        }

        val message = "There was no output directory provided for JFlex to write into."
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }
        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given generate is called, it configures JFlex`() {
        mockkConstructor(LexGenerator::class)
        mockkStatic(Skeleton::class)

        // Given
        val flexFile = project.file("somewhere/something.l")
        val outPutDir = project.file("somewhere/else")
        val encoding: String = Charsets.ISO_8859_1.name()
        val flag = true

        val task = project.tasks.create("sut", JFlexTask::class.java) {}
        task.flexFile.set(flexFile)
        task.outputDirectory.set(outPutDir)

        task.backup.set(flag)
        task.disableUnusedWarning.set(flag)
        task.showDump.set(flag)
        task.encoding.set(encoding)
        task.generateDotFile.set(flag)
        task.showTimeStatics.set(flag)
        task.useLegacyDot.set(flag)
        task.progressDots.set(flag)
        task.useJLexCompatibilityMode.set(flag)
        task.minimize.set(!flag)
        task.verbose.set(flag)

        every { Skeleton.readDefault() } just Runs
        every {
            constructedWith<LexGenerator>(OfTypeMatcher<File>(File::class)).generate()
        } returns fixture()

        // When
        task.generate()

        verify(exactly = 1) { Skeleton.readDefault() }
        assertEquals(
            actual = Options.getRootDirectory(),
            expected = File("")
        )
        assertEquals(
            actual = Options.directory,
            expected = outPutDir
        )
        assertEquals(
            actual = Options.encoding,
            expected = Charset.forName(encoding)
        )
        assertEquals(
            actual = Options.verbose,
            expected = flag
        )
        assertEquals(
            actual = Options.unused_warning,
            expected = !flag
        )
        assertEquals(
            actual = Options.jlex,
            expected = flag
        )
        assertEquals(
            actual = Options.no_minimize,
            expected = flag
        )
        assertEquals(
            actual = Options.no_backup,
            expected = !flag
        )
        assertEquals(
            actual = Options.time,
            expected = flag
        )
        assertEquals(
            actual = Options.dot,
            expected = flag
        )
        assertEquals(
            actual = Options.dump,
            expected = flag
        )
        assertEquals(
            actual = Options.legacy_dot,
            expected = flag
        )
        assertEquals(
            actual = Options.progress,
            expected = flag
        )

        unmockkStatic(Skeleton::class)
        unmockkConstructor(LexGenerator::class)
    }

    @Test
    fun `Given generate is called, it uses a custom Skeleton File if provided`() {
        mockkConstructor(LexGenerator::class)
        mockkStatic(Skeleton::class)

        // Given
        val flexFile = project.file("somewhere/something.l")
        val outPutDir = project.file("somewhere/else")
        val skeleton = project.file("somewhere/skeleton.skel")
        val encoding: String = Charsets.ISO_8859_1.name()
        val flag: Boolean = fixture()

        val task = project.tasks.create("sut", JFlexTask::class.java) {}
        task.flexFile.set(flexFile)
        task.customSkeletonFile.set(skeleton)
        task.outputDirectory.set(outPutDir)

        task.backup.set(flag)
        task.disableUnusedWarning.set(flag)
        task.showDump.set(flag)
        task.encoding.set(encoding)
        task.generateDotFile.set(flag)
        task.showTimeStatics.set(flag)
        task.useLegacyDot.set(flag)
        task.progressDots.set(flag)
        task.useJLexCompatibilityMode.set(flag)
        task.verbose.set(flag)

        every { Skeleton.readSkelFile(any()) } just Runs
        every {
            constructedWith<LexGenerator>(OfTypeMatcher<File>(File::class)).generate()
        } returns fixture()

        // When
        task.generate()

        verify(exactly = 1) {
            Skeleton.readSkelFile(skeleton)
        }

        unmockkStatic(Skeleton::class)
        unmockkConstructor(LexGenerator::class)
    }

    @Test
    fun `Given generate is called it fails if the underlining Generator fails`() {
        mockkConstructor(LexGenerator::class)
        mockkStatic(Skeleton::class)
        mockkStatic(Logging::class)

        // Given
        val flexFile = project.file("somewhere/something.l")
        val outPutDir = project.file("somewhere/else")
        val nestedError = RuntimeException("error")

        val task = project.tasks.create("sut", JFlexTask::class.java) {}
        task.flexFile.set(flexFile)
        task.outputDirectory.set(outPutDir)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(JFlexTask::class.java) } returns logger
        every { logger.info(any()) } just Runs

        every { Skeleton.readSkelFile(any()) } just Runs
        every {
            constructedWith<LexGenerator>(OfTypeMatcher<File>(File::class)).generate()
        } throws nestedError

        // Then
        val error = assertFailsWith<JFlexTaskError.CodeGenerationRuntimeError> {
            // When
            task.generate()
        }

        val message = "Something went wrong during code generation:\n${nestedError.message}"
        assertEquals(
            actual = error.message,
            expected = message
        )

        unmockkConstructor(LexGenerator::class)
        unmockkStatic(Skeleton::class)
        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given generate is called it just runs if the Generation is a success`() {
        mockkConstructor(LexGenerator::class)
        mockkStatic(Skeleton::class)
        mockkStatic(Logging::class)

        // Given
        val flexFile = project.file("somewhere/something.l")
        val outPutDir = project.file("somewhere/else")

        val task = project.tasks.create("sut", JFlexTask::class.java) {}
        task.flexFile.set(flexFile)
        task.outputDirectory.set(outPutDir)

        val outputFilePath: String = fixture()

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(JFlexTask::class.java) } returns logger
        every { logger.info(any()) } just Runs

        every { Skeleton.readSkelFile(any()) } just Runs
        every {
            constructedWith<LexGenerator>(EqMatcher(flexFile)).generate()
        } returns outputFilePath

        // When
        task.generate()

        // Then
        verify(exactly = 1) { logger.info("Start code generation of ${flexFile.absolutePath}") }
        verify(exactly = 1) { logger.info("JFlex was successful and wrote the output into $outputFilePath") }
        verify(exactly = 1) { constructedWith<LexGenerator>(EqMatcher(flexFile)).generate() }

        unmockkConstructor(LexGenerator::class)
        unmockkStatic(Skeleton::class)
        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given generate is called it and the Generation is a success it does not print any statisics by default`() {
        mockkConstructor(LexGenerator::class)
        mockkStatic(Skeleton::class)
        mockkStatic(Logging::class)
        mockkStatic(Out::class)

        // Given
        val flexFile = project.file("somewhere/something.l")
        val outPutDir = project.file("somewhere/else")

        val task = project.tasks.create("sut", JFlexTask::class.java) {}
        task.flexFile.set(flexFile)
        task.outputDirectory.set(outPutDir)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(JFlexTask::class.java) } returns logger
        every { logger.info(any()) } just Runs

        every { Skeleton.readSkelFile(any()) } just Runs
        every {
            constructedWith<LexGenerator>(EqMatcher(flexFile)).generate()
        } returns fixture()
        every { Out.statistics() } just Runs

        // When
        task.generate()

        // Then
        verify(exactly = 0) { Out.statistics() }

        unmockkConstructor(LexGenerator::class)
        unmockkStatic(Skeleton::class)
        unmockkStatic(Logging::class)
        unmockkStatic(Out::class)
    }

    @Test
    fun `Given generate is called it and the Generation is a success it prints the statisics if showStatisics is true`() {
        mockkConstructor(LexGenerator::class)
        mockkStatic(Skeleton::class)
        mockkStatic(Logging::class)
        mockkStatic(Out::class)

        // Given
        val flexFile = project.file("somewhere/something.l")
        val outPutDir = project.file("somewhere/else")

        val task = project.tasks.create("sut", JFlexTask::class.java) {}
        task.flexFile.set(flexFile)
        task.outputDirectory.set(outPutDir)
        task.showStatistic.set(true)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(JFlexTask::class.java) } returns logger
        every { logger.info(any()) } just Runs

        every { Skeleton.readSkelFile(any()) } just Runs
        every {
            constructedWith<LexGenerator>(EqMatcher(flexFile)).generate()
        } returns fixture()
        every { Out.statistics() } just Runs

        // When
        task.generate()

        // Then
        verify(exactly = 1) { Out.statistics() }

        unmockkConstructor(LexGenerator::class)
        unmockkStatic(Skeleton::class)
        unmockkStatic(Logging::class)
        unmockkStatic(Out::class)
    }
}
