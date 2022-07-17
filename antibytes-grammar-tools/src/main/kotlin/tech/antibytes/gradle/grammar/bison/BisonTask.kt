/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar.bison

import com.lordcodes.turtle.shellRun
import kotlin.jvm.Throws
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.grammar.BisonTaskContract

abstract class BisonTask : BisonTaskContract, DefaultTask() {
    init {
        executable.convention { project.file("/usr/bin/bison") }
        debug.convention(false)
        locations.convention(false)
        noLines.convention(false)
        update.convention(false)
        tokenTable.convention(false)
        verbose.convention(false)

        report.convention(emptySet())
        features.convention(emptySet())
        warnings.convention(emptySet())
        errors.convention(emptySet())

        define.convention(emptyMap())
        forceDefine.convention(emptyMap())
        filePrefixMap.convention(emptyMap())
    }

    @Throws(BisonTaskError::class)
    private fun checkRequiredParameter(logger: Logger) {
        if (!executable.isPresent) {
            logger.error(BisonTaskError.MISSING_BISON_EXEC)
            throw BisonTaskError.MissingBisonExecError()
        }

        if (!grammarFile.isPresent) {
            logger.error(BisonTaskError.MISSING_GRAMMAR_FILE)
            throw BisonTaskError.MissingGrammarFileError()
        }

        if (!outputFile.isPresent) {
            logger.error(BisonTaskError.MISSING_OUTPUT_FILE)
            throw BisonTaskError.MissingOutputFileError()
        }
    }

    private fun addBooleanOptions(arguments: MutableList<String>) {
        if (debug.get()) {
            arguments.add("--debug")
        }

        if (locations.get()) {
            arguments.add("--locations")
        }

        if (noLines.get()) {
            arguments.add("--no-lines")
        }

        if (update.get()) {
            arguments.add("--update")
        }

        if (tokenTable.get()) {
            arguments.add("--token-table")
        }

        if (verbose.get()) {
            arguments.add("--verbose")
        }
    }

    private fun addFileOptions(arguments: MutableList<String>) {
        if (customSkeletonFile.isPresent) {
            arguments.add("--skeleton=${customSkeletonFile.get().asFile.absolutePath}")
        }

        if (header.isPresent) {
            arguments.add("--header=${header.get().asFile.absolutePath}")
        }

        if (reportFile.isPresent) {
            arguments.add("--report-file=${reportFile.get().asFile.absolutePath}")
        }

        if (graphFile.isPresent) {
            arguments.add("--graph=${graphFile.get().asFile.absolutePath}")
        }

        if (xmlReport.isPresent) {
            arguments.add("--xml=${xmlReport.get().asFile.absolutePath}")
        }

        if (style.isPresent) {
            arguments.add("--style=${style.get().asFile.absolutePath}")
        }
    }

    private fun addStringOptions(arguments: MutableList<String>) {
        if (namePrefix.isPresent) {
            arguments.add("--name-prefix=${namePrefix.get()}")
        }

        if (filePrefix.isPresent) {
            arguments.add("--file-prefix=${filePrefix.get()}")
        }

        if (report.get().isNotEmpty()) {
            val reportOptions = report.get().map { option -> option.value }
            arguments.add("--report=${reportOptions.joinToString(",")}")
        }

        if (language.isPresent) {
            arguments.add("--language=${language.get()}")
        }

        if (features.get().isNotEmpty()) {
            val featureOptions = features.get().map { option -> option.value }
            arguments.add("--feature=${featureOptions.joinToString(",")}")
        }

        if (color.isPresent) {
            arguments.add("--color=${color.get().value}")
        }
    }

    private fun concatenateMapOption(map: Map<String, String>): String {
        return map
            .map { (key, value) -> "$key=$value" }
            .joinToString(",")
    }

    private fun addMapOptions(arguments: MutableList<String>) {
        if (define.get().isNotEmpty()) {
            arguments.add("--define=${concatenateMapOption(define.get())}")
        }

        if (forceDefine.get().isNotEmpty()) {
            arguments.add("--force-define=${concatenateMapOption(forceDefine.get())}")
        }

        if (filePrefixMap.get().isNotEmpty()) {
            arguments.add("--file-prefix-map=${concatenateMapOption(filePrefixMap.get())}")
        }
    }

    private fun addErrorOptions(arguments: MutableList<String>) {
        var warnings = if (warnings.get().isNotEmpty()) {
            warnings.get()
                .map { option -> option.value }
                .joinToString(",")
        } else {
            ""
        }

        if (errors.get().isNotEmpty()) {
            val errors = errors.get()
                .map { option -> "error=${option.value}" }
                .joinToString(",")
            warnings += ",$errors"
        }

        if (warnings.isNotEmpty()) {
            arguments.add("--warning=${warnings.trimStart(',')}")
        }
    }

    private fun assembleOptions(): MutableList<String> {
        val arguments = mutableListOf<String>()

        addBooleanOptions(arguments)
        addFileOptions(arguments)
        addStringOptions(arguments)
        addMapOptions(arguments)
        addErrorOptions(arguments)

        return arguments
    }

    private fun assembleArgument(): List<String> {
        val arguments = assembleOptions()

        arguments.add("--output=${outputFile.get().asFile.absolutePath}")
        arguments.add(grammarFile.get().asFile.absolutePath.toString())

        return arguments
    }

    private fun runBison(logger: Logger) {
        logger.info("Start code generation of ${grammarFile.get().asFile.name}")

        val stdout = try {
            shellRun(
                command = executable.get().asFile.absolutePath,
                arguments = assembleArgument(),
            )
        } catch (error: Exception) {
            logger.error("${BisonTaskError.CODE_GENERATION_RUNTIME_ERROR}\n${error.message}")
            throw BisonTaskError.CodeGenerationRuntimeError(error.message)
        }

        logger.info(
            "Bison was successful and wrote the output into ${outputFile.get().asFile.name}.\nIt produced the following reports:\n$stdout",
        )
    }

    @TaskAction
    @Throws(BisonTaskError::class)
    override fun generate() {
        val logger = Logging.getLogger(BisonTask::class.java)

        checkRequiredParameter(logger)
        runBison(logger)
    }
}
