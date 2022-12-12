/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

/* ktlint-disable filename */
package tech.antibytes.gradle.grammar

import kotlin.jvm.Throws
import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.grammar.jflex.JFlexTaskError

interface JFlexTaskContract : Task {
    /**
     * The grammar file to be processed
     * This property is required
     */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    val flexFile: RegularFileProperty

    /**
     * Directory where the generated file will be written
     * This property is required
     */
    @get:OutputDirectory
    val outputDirectory: DirectoryProperty

    /**
     * Flag, which will causes generation process messages to be displayed
     * The default is false
     */
    @get:Input
    val encoding: Property<String>

    /**
     * Location of an custom skeleton file UTF-8 encoded.
     * This is an optional property
     */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:Optional
    val customSkeletonFile: RegularFileProperty

    /**
     * Flag, which will causes generation process messages to be displayed
     * The default is false
     */
    @get:Input
    val verbose: Property<Boolean>

    /**
     * Flag, which will causes character classes, NFA and DFA to be dumped and display
     * The default is false
     */
    @get:Input
    val showDump: Property<Boolean>

    /**
     * Flag, which will causes the generated time statistics to be displayed
     * The default is false
     */
    @get:Input
    val showTimeStatics: Property<Boolean>

    /**
     * Flag, which will causes graphviz .dot files for the generated automata to be added
     * The default is false
     */
    @get:Input
    val generateDotFile: Property<Boolean>

    /**
     * Flag, which will causes the generated file if it already exists  to be back upped
     * The default is false
     */
    @get:Input
    val backup: Property<Boolean>

    /**
     * Flag, which will causes the minimization of the resulting DFA
     * The default is true
     */
    @get:Input
    val minimize: Property<Boolean>

    /**
     * Flag, which enables compatibility mode for [JLex](https://www.cs.princeton.edu/~appel/modern/java/JLex/)
     * The default is false
     */
    @get:Input
    val useJLexCompatibilityMode: Property<Boolean>

    /**
     * Flag, which causes dot . meta-character matches [ˆ\n] instead of [ˆ\n\r\u000B\u000C\u0085\u2028\u2029] to be used
     * The default is false
     */
    @get:Input
    val useLegacyDot: Property<Boolean>

    /**
     * Flag, which causes unused warning to be disabled
     * The default is false
     */
    @get:Input
    val disableUnusedWarning: Property<Boolean>

    /**
     * Flag, which causes the JFlex statistic to be printed
     * The default is false
     */
    @get:Input
    val showStatistic: Property<Boolean>

    /**
     * Flag, which causes to print dots as progress bar
     * The default is false
     */
    @get:Input
    val progressDots: Property<Boolean>

    /**
     * Generates a Java file of the provided grammar and parameters to the provided target location
     * @throws MissingFlexFileError if no valid grammar file was provided
     * @throws MissingOutputDirectoryError if no output directory was provided
     * @throws CodeGenerationRuntimeError if flex fails generate any output
     */
    @TaskAction
    @Throws(JFlexTaskError::class)
    fun generate()
}
