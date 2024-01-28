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

    private fun errorIf(
        condition: Boolean,
        logger: Logger,
        message: String,
        error: BisonTaskError,
    ) {
        if (condition) {
            logger.error(message)
            throw error
        }
    }

    @Throws(BisonTaskError::class)
    private fun checkRequiredParameter(logger: Logger) {
        errorIf(!executable.isPresent, logger, BisonTaskError.MISSING_BISON_EXEC, BisonTaskError.MissingBisonExecError())
        errorIf(!grammarFile.isPresent, logger, BisonTaskError.MISSING_GRAMMAR_FILE, BisonTaskError.MissingGrammarFileError())
        errorIf(!outputFile.isPresent, logger, BisonTaskError.MISSING_OUTPUT_FILE, BisonTaskError.MissingOutputFileError())
    }

    private fun addBooleanOptions(arguments: MutableList<String>) {
        arguments.addIf(debug.get(), "--debug")
        arguments.addIf(locations.get(), "--locations")
        arguments.addIf(noLines.get(), "--no-lines")
        arguments.addIf(update.get(), "--update")
        arguments.addIf(tokenTable.get(), "--token-table")
        arguments.addIf(verbose.get(), "--verbose")
    }

    private fun MutableList<String>.addIf(
        condition: Boolean,
        value: String,
    ) {
        if (condition) {
            add(value)
        }
    }

    private fun addFileOptions(arguments: MutableList<String>) {
        arguments.addIf(customSkeletonFile.isPresent, "--skeleton=${customSkeletonFile.get().asFile.absolutePath}")
        arguments.addIf(header.isPresent, "--header=${header.get().asFile.absolutePath}")
        arguments.addIf(reportFile.isPresent, "--report-file=${reportFile.get().asFile.absolutePath}")
        arguments.addIf(graphFile.isPresent, "--graph=${graphFile.get().asFile.absolutePath}")
        arguments.addIf(xmlReport.isPresent, "--xml=${xmlReport.get().asFile.absolutePath}")
        arguments.addIf(style.isPresent, "--style=${style.get().asFile.absolutePath}")
    }

    private fun addStringOptions(arguments: MutableList<String>) {
        arguments.addIf(namePrefix.isPresent, "--name-prefix=${namePrefix.get()}")
        arguments.addIf(filePrefix.isPresent, "--file-prefix=${filePrefix.get()}")

        if (report.get().isNotEmpty()) {
            val reportOptions = report.get().map { option -> option.value }
            arguments.add("--report=${reportOptions.joinToString(",")}")
        }

        arguments.addIf(language.isPresent, "--language=${language.get()}")

        if (features.get().isNotEmpty()) {
            val featureOptions = features.get().map { option -> option.value }
            arguments.add("--feature=${featureOptions.joinToString(",")}")
        }

        arguments.addIf(color.isPresent, "--color=${color.get().value}")
    }

    private fun concatenateMapOption(map: Map<String, String>): String {
        return map
            .map { (key, value) -> "$key=$value" }
            .joinToString(",")
    }

    private fun addMapOptions(arguments: MutableList<String>) {
        arguments.addIf(define.get().isNotEmpty(), "--define=${concatenateMapOption(define.get())}")
        arguments.addIf(forceDefine.get().isNotEmpty(), "--force-define=${concatenateMapOption(forceDefine.get())}")
        arguments.addIf(filePrefixMap.get().isNotEmpty(), "--file-prefix-map=${concatenateMapOption(filePrefixMap.get())}")
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
