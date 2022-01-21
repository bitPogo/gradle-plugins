/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar.bison

import com.appmattus.kotlinfixture.kotlinFixture
import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.shellRun
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
import org.junit.Test
import tech.antibytes.gradle.grammar.BisonTaskContract
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BisonTaskSpec {
    private lateinit var project: Project
    private val fixture = kotlinFixture()

    @Before
    fun setup() {
        project = ProjectBuilder.builder().build()
    }

    @Test
    fun `It fulfils BisonTaskContract`() {
        val task: Any = project.tasks.create("sut", BisonTask::class.java) {}

        assertTrue(task is BisonTaskContract)
    }

    @Test
    fun `It has no default Executable`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.executable.get().asFile.absolutePath,
            expected = "/usr/bin/bison"
        )
    }

    @Test
    fun `It has no default GrammarFile`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.grammarFile.isPresent)
    }

    @Test
    fun `It has no default OutputFile`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.outputFile.isPresent)
    }

    @Test
    fun `It has no default CustomSkeletonFile`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.customSkeletonFile.isPresent)
    }

    @Test
    fun `It has false as Default for Debug`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.debug.get())
    }

    @Test
    fun `It has false as Default for Locations`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.locations.get())
    }

    @Test
    fun `It has no default NamePrefix`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.namePrefix.isPresent)
    }

    @Test
    fun `It has false as Default for NoLines`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.noLines.get())
    }

    @Test
    fun `It has false as Default for Update`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.update.get())
    }

    @Test
    fun `It has false as Default for TokenTable`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.tokenTable.get())
    }

    @Test
    fun `It has no default Header`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.header.isPresent)
    }

    @Test
    fun `It has a empty Map as default Define`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.define.get(),
            expected = emptyMap<String, String>()
        )
    }

    @Test
    fun `It has a empty Map as default ForceDefine`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.forceDefine.get(),
            expected = emptyMap<String, String>()
        )
    }

    @Test
    fun `It has no default FilePrefix`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.filePrefix.isPresent)
    }

    @Test
    fun `It has an empty Set as default Report`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.report.get(),
            expected = emptySet<BisonTaskContract.Report>()
        )
    }

    @Test
    fun `It has false as default Verbose`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.verbose.get())
    }

    @Test
    fun `It has no default GraphFile`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.graphFile.isPresent)
    }

    @Test
    fun `It has no default ReportFile`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.reportFile.isPresent)
    }

    @Test
    fun `It has no default XmlFile`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.xmlReport.isPresent)
    }

    @Test
    fun `It has a empty Map as default FilePrefixMap`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.filePrefixMap.get(),
            expected = emptyMap()
        )
    }

    @Test
    fun `It has no default Language`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.language.isPresent)
    }

    @Test
    fun `It has a empty Set as default Features`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.features.get(),
            expected = emptySet()
        )
    }

    @Test
    fun `It has a empty Set as default Warnings`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.warnings.get(),
            expected = emptySet()
        )
    }

    @Test
    fun `It has a empty Set as default Errors`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertEquals(
            actual = task.errors.get(),
            expected = emptySet()
        )
    }

    @Test
    fun `It has no default Color`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.color.isPresent)
    }

    @Test
    fun `It has no default Style`() {
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        assertFalse(task.style.isPresent)
    }

    @Test
    fun `Given generate is called, it fails and logs the Error if no Bison executable file was given`() {
        mockkStatic(Logging::class)
        // Given
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        task.executable.convention(null)
        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        // Then
        val error = assertFailsWith<BisonTaskError.MissingBisonExecError> {
            // When
            task.generate()
        }

        val message = "There was no Bison executable provided."
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }

        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given generate is called, it fails and logs the Error if no Grammar file was given`() {
        mockkStatic(Logging::class)
        // Given
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        val bisonExec = project.file("somewhere/bison")

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        // Then
        task.executable.set(bisonExec)
        val error = assertFailsWith<BisonTaskError.MissingGrammarFileError> {
            // When
            task.generate()
        }

        val message = "There was no grammar file provided for Bison to process."
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }

        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given generate is called, it fails and logs the Error if no Output file was given`() {
        mockkStatic(Logging::class)
        // Given
        val task = project.tasks.create("sut", BisonTask::class.java) {}

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        // Then
        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        val error = assertFailsWith<BisonTaskError.MissingOutputFileError> {
            // When
            task.generate()
        }

        val message = "There was no output file provided for Bison to write into."
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }

        unmockkStatic(Logging::class)
    }

    @Test
    fun `Given generate is called and all required parameter are set, it fails if the Bison command fails`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } throws ShellFailedException(RuntimeException("test"))

        // Then
        val error = assertFailsWith<BisonTaskError.CodeGenerationRuntimeError> {
            // When
            task.generate()
        }

        val message = "Something went wrong during code generation:\nRunning shell command failed"
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it fails if the Bison command fails, while the nested error was null`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } throws RuntimeException()

        // Then
        val error = assertFailsWith<BisonTaskError.CodeGenerationRuntimeError> {
            // When
            task.generate()
        }

        val message = "Something went wrong during code generation:\nnull"
        assertEquals(
            actual = error.message,
            expected = message
        )
        verify(exactly = 1) { logger.error(message) }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it executes the Bison command`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}
        val bisonOutput: String = fixture()

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } returns bisonOutput

        // When
        task.generate()

        verify(exactly = 1) {
            shellRun(
                command = bisonExec.absolutePath,
                arguments = listOf(
                    "--output=${outputFile.absolutePath}",
                    grammarFile.absolutePath
                )
            )
        }
        verify(exactly = 1) { logger.info("Start code generation of ${grammarFile.name}") }
        verify(exactly = 1) {
            logger.info(
                "Bison was successful and wrote the output into ${outputFile.name}.\nIt produced the following reports:\n$bisonOutput"
            )
        }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it executes the Bison command with binary Options if they are true`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}
        val bisonOutput: String = fixture()

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)
        task.debug.set(true)
        task.locations.set(true)
        task.noLines.set(true)
        task.update.set(true)
        task.tokenTable.set(true)
        task.verbose.set(true)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } returns bisonOutput

        // When
        task.generate()

        verify(exactly = 1) {
            shellRun(
                command = bisonExec.absolutePath,
                arguments = listOf(
                    "--debug",
                    "--locations",
                    "--no-lines",
                    "--update",
                    "--token-table",
                    "--verbose",
                    "--output=${outputFile.absolutePath}",
                    grammarFile.absolutePath
                )
            )
        }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it executes the Bison command with String or File based Options`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}
        val bisonOutput: String = fixture()

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        val filePrefix: String = fixture()
        val namePrefix: String = fixture()
        val language: String = fixture()

        val report = listOf(
            BisonTaskContract.Report.ITEM_SET,
            BisonTaskContract.Report.LOOKAHEAD,
        )
        val features = listOf(
            BisonTaskContract.Features.SYNTAX_ONLY
        )
        val color = BisonTaskContract.When.ALWAYS

        val skeleton = project.file("somewhere/skeleton.skel")
        val graphFile = project.file("somewhere/graph")
        val defines = project.file("somewhere/defines")
        val reportFile = project.file("somewhere/report")
        val xmlReport = project.file("somewhere/xml")
        val style = project.file("somewhere/css")

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)

        task.filePrefix.set(filePrefix)
        task.namePrefix.set(namePrefix)
        task.report.set(report)
        task.customSkeletonFile.set(skeleton)
        task.header.set(defines)
        task.graphFile.set(graphFile)
        task.reportFile.set(reportFile)
        task.xmlReport.set(xmlReport)
        task.language.set(language)
        task.features.set(features)
        task.color.set(color)
        task.style.set(style)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } returns bisonOutput

        // When
        task.generate()

        verify(exactly = 1) {
            shellRun(
                command = bisonExec.absolutePath,
                arguments = listOf(
                    "--skeleton=${skeleton.absolutePath}",
                    "--header=${defines.absolutePath}",
                    "--report-file=${reportFile.absolutePath}",
                    "--graph=${graphFile.absolutePath}",
                    "--xml=${xmlReport.absolutePath}",
                    "--style=${style.absolutePath}",
                    "--name-prefix=$namePrefix",
                    "--file-prefix=$filePrefix",
                    "--report=itemset,look-a-head",
                    "--language=$language",
                    "--feature=syntax-only",
                    "--color=always",
                    "--output=${outputFile.absolutePath}",
                    grammarFile.absolutePath
                )
            )
        }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it executes the Bison command with Map based Options`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}
        val bisonOutput: String = fixture()

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        val define = mapOf(
            "a" to "b",
            "c" to "d"
        )

        val forceDefine = mapOf(
            "e" to "f",
            "g" to "h"
        )

        val filePrefix = mapOf(
            "m" to "n"
        )

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)
        task.define.set(define)
        task.forceDefine.set(forceDefine)
        task.filePrefixMap.set(filePrefix)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } returns bisonOutput

        // When
        task.generate()

        verify(exactly = 1) {
            shellRun(
                command = bisonExec.absolutePath,
                arguments = listOf(
                    "--define=a=b,c=d",
                    "--force-define=e=f,g=h",
                    "--file-prefix-map=m=n",
                    "--output=${outputFile.absolutePath}",
                    grammarFile.absolutePath
                )
            )
        }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it executes the Bison command with Warning parameter`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}
        val bisonOutput: String = fixture()

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        val warnings = setOf(
            BisonTaskContract.ErrorCategory.COUNTER_EXAMPLES,
            BisonTaskContract.ErrorCategory.EMPTY_RULE
        )

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)
        task.warnings.set(warnings)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } returns bisonOutput

        // When
        task.generate()

        verify(exactly = 1) {
            shellRun(
                command = bisonExec.absolutePath,
                arguments = listOf(
                    "--warning=counterexamples,empty-rule",
                    "--output=${outputFile.absolutePath}",
                    grammarFile.absolutePath
                )
            )
        }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it executes the Bison command with Error parameter`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}
        val bisonOutput: String = fixture()

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        val errors = setOf(
            BisonTaskContract.ErrorCategory.COUNTER_EXAMPLES,
            BisonTaskContract.ErrorCategory.EMPTY_RULE
        )

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)
        task.errors.set(errors)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } returns bisonOutput

        // When
        task.generate()

        verify(exactly = 1) {
            shellRun(
                command = bisonExec.absolutePath,
                arguments = listOf(
                    "--warning=error=counterexamples,error=empty-rule",
                    "--output=${outputFile.absolutePath}",
                    grammarFile.absolutePath
                )
            )
        }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }

    @Test
    fun `Given generate is called and all required parameter are set, it executes the Bison command with Error and Warning parameter`() {
        mockkStatic(Logging::class)
        mockkStatic("com.lordcodes.turtle.ShellKt")

        val task = project.tasks.create("sut", BisonTask::class.java) {}
        val bisonOutput: String = fixture()

        val bisonExec = project.file("somewhere/bison")
        val grammarFile = project.file("somewhere/something.y")
        val outputFile = project.file("somewhere/somekotlin.kt")

        val errors = setOf(
            BisonTaskContract.ErrorCategory.ALL
        )

        val warnings = setOf(
            BisonTaskContract.ErrorCategory.COUNTER_EXAMPLES,
            BisonTaskContract.ErrorCategory.EMPTY_RULE
        )

        task.executable.set(bisonExec)
        task.grammarFile.set(grammarFile)
        task.outputFile.set(outputFile)
        task.warnings.set(warnings)
        task.errors.set(errors)

        val logger: Logger = mockk(relaxUnitFun = true)
        every { Logging.getLogger(BisonTask::class.java) } returns logger

        every { shellRun(any<String>(), any()) } returns bisonOutput

        // When
        task.generate()

        verify(exactly = 1) {
            shellRun(
                command = bisonExec.absolutePath,
                arguments = listOf(
                    "--warning=counterexamples,empty-rule,error=all",
                    "--output=${outputFile.absolutePath}",
                    grammarFile.absolutePath
                )
            )
        }

        unmockkStatic(Logging::class)
        unmockkStatic("com.lordcodes.turtle.ShellKt")
    }
}
