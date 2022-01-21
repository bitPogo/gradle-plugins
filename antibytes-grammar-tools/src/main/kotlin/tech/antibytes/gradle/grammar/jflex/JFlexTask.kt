/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar.jflex

import jflex.generator.LexGenerator
import jflex.logging.Out
import jflex.option.Options
import jflex.skeleton.Skeleton
import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.grammar.JFlexTaskContract
import tech.antibytes.gradle.grammar.jflex.JFlexTaskError.Companion.MISSING_FLEX_FILE
import tech.antibytes.gradle.grammar.jflex.JFlexTaskError.Companion.MISSING_OUTPUT_DIRECTORY
import java.io.File
import java.nio.charset.Charset
import kotlin.jvm.Throws

@CacheableTask
abstract class JFlexTask : JFlexTaskContract, DefaultTask() {
    init {
        backup.convention(false)
        disableUnusedWarning.convention(false)
        showDump.convention(false)
        encoding.convention(Charsets.UTF_8.name())
        generateDotFile.convention(false)
        useJLexCompatibilityMode.convention(false)
        useLegacyDot.convention(false)
        minimize.convention(true)
        progressDots.convention(false)
        showTimeStatics.convention(false)
        showStatistic.convention(false)
        verbose.convention(false)
    }

    @Throws(JFlexTaskError::class)
    private fun checkRequiredParameter(logger: Logger) {
        if (!flexFile.isPresent) {
            logger.error(MISSING_FLEX_FILE)
            throw JFlexTaskError.MissingFlexFileError()
        }

        if (!outputDirectory.isPresent) {
            logger.error(MISSING_OUTPUT_DIRECTORY)
            throw JFlexTaskError.MissingOutputDirectoryError()
        }
    }

    private fun setOptions() {
        Options.setRootDirectory(rootDirectory)
        Options.no_backup = !backup.get()
        Options.directory = outputDirectory.get().asFile
        Options.unused_warning = !disableUnusedWarning.get()
        Options.dump = showDump.get()
        Options.encoding = Charset.forName(encoding.get())
        Options.dot = generateDotFile.get()
        Options.jlex = useJLexCompatibilityMode.get()
        Options.legacy_dot = useLegacyDot.get()
        Options.no_minimize = !minimize.get()
        Options.progress = progressDots.get()
        Options.time = showTimeStatics.get()
        Options.verbose = verbose.get()
    }

    private fun setSkeletonFile() {
        if (customSkeletonFile.isPresent) {
            Skeleton.readSkelFile(customSkeletonFile.get().asFile)
        } else {
            Skeleton.readDefault()
        }
    }

    private fun generate(logger: Logger) {
        logger.info(
            "Start code generation of ${flexFile.get().asFile.absolutePath}"
        )

        val outputFilePath = try {
            LexGenerator(flexFile.get().asFile).generate()
        } catch (error: Exception) {
            logger.error("${JFlexTaskError.CODE_GENERATION_RUNTIME_ERROR}\n${error.message}")
            throw JFlexTaskError.CodeGenerationRuntimeError(error.message)
        }

        logger.info(
            "JFlex was successful and wrote the output into $outputFilePath"
        )
    }

    private fun tearDown() {
        if (showStatistic.get()) {
            Out.statistics()
        }
    }

    @Throws(JFlexTaskError::class)
    @TaskAction
    override fun generate() {
        val logger = Logging.getLogger(JFlexTask::class.java)

        checkRequiredParameter(logger)
        setOptions()
        setSkeletonFile()
        generate(logger)
        tearDown()
    }

    private companion object {
        @JvmStatic
        val rootDirectory = File("")
    }
}
