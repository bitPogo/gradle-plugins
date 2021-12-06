/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar

import org.gradle.api.tasks.Input
import org.gradle.api.Task
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.grammar.bison.BisonTaskError
import kotlin.jvm.Throws

interface BisonTaskContract : Task {
    enum class Report(val value: String) {
        /**
         * Description of the grammar, conflicts (resolved and unresolved), and parser’s automaton.
         */
        STATE("state"),

        /**
         * Implies state and augments the description of the automaton with each rule's look-ahead set.
         */
        LOOKAHEAD("look-a-head"),

        /**
         * Implies state and augments the description of the automaton with the full set of items for each state, instead of its core only.
         */
        ITEM_SET("itemset"),

        /**
         * Implies state. Explain how conflicts were solved thanks to precedence and associativity directives.
         */
        SOLVED("solved"),

        /**
         * Look for counterexamples for the conflicts. See Generation of Counterexamples. Counterexamples take time to compute. The option -rcex should be used by the developer when working on the grammar; it hardly makes sense to use it in a CI.
         */
        COUNTER_EXAMPLES("counterexamples"),

        /**
         * Enable all the items.
         */
        ALL("all"),

        /**
         * Do not generate the report.
         */
        NONE("none")
    }

    enum class Features(val value: String) {
        /**
         * show errors with carets
         */
        CARET("caret"),

        /**
         * show errors with carets
         */
        SHOW_CARET("diagnostics-show-caret"),

        /**
         * machine-readable fixes
         */
        FIX_IT("fixit"),

        /**
         * machine-readable fixes
         */
        PARSABLE_FIXES("diagnostics-parseable-fixits"),

        /**
         * do not generate any file
         */
        SYNTAX_ONLY("syntax-only"),

        /**
         * Enable all the items.
         */
        ALL("all"),

        /**
         * Do not generate the report.
         */
        NONE("none")
    }

    enum class ErrorCategory(val value: String) {
        /**
         * shift-reduce conflicts (enabled by default)
         */
        SR_CONFLICTS("conflicts-sr"),

        /**
         * Turn off SR-Category
         */
        NO_SR_CONFLICTS("no-conflicts-sr"),

        /**
         * reduce-reduce conflicts (enabled by default)
         */
        RR_CONFLICTS("conflicts-rr"),

        /**
         * Turn off RR-Category
         */
        NO_RR_CONFLICTS("no-conflicts-rr"),

        /**
         * generate conflict counterexamples
         */
        COUNTER_EXAMPLES("counterexamples"),

        /**
         * Turn off counterexamples Category
         */
        NO_COUNTER_EXAMPLES("no-counterexamples"),

        /**
         * string aliases not attached to a symbol
         */
        DANGLING_ALIAS("dangling-alias"),

        /**
         * Turn off dangling alias Category
         */
        NO_DANGLING_ALIAS("no-dangling-alias"),

        /**
         * obsolete constructs
         */
        DEPRECATED("deprecated"),

        /**
         * Turn off deprecated Category
         */
        NO_DEPRECATED("no-deprecated"),

        /**
         * empty rules without %empty
         */
        EMPTY_RULE("empty-rule"),

        /**
         * Turn off empty rules Category
         */
        NO_EMPTY_RULE("no-empty-rule"),

        /**
         * unset or unused mid rule values
         */
        MID_RULE_VALUES("midrule-values"),

        /**
         * Turn off mid rule values Category
         */
        NO_MID_RULE_VALUES("no-midrule-values"),

        /**
         * useless precedence and associativity
         */
        PRECEDENCE("precedence"),

        /**
         * Turn off precedence Category
         */
        NO_PRECEDENCE("no-precedence"),

        /**
         * incompatibilities with POSIX Yacc
         */
        YACC("yacc"),

        /**
         * Turn off yacc Category
         */
        NO_YACC("no-yacc"),

        /**
         *  all other warnings (enabled by default)
         */
        OTHER("other"),

        /**
         * Turn off other Category
         */
        NO_OTHER("no-other"),

        /**
         * all the warnings except 'counterexamples', 'dangling-alias' and 'yacc'
         */
        ALL("all"),

        /**
         * turn off all the warnings
         */
        NONE("none")
    }

    enum class When(val value: String) {
        /**
         * colorize the output
         */
        ALWAYS("always"),

        /**
         * don't colorize the output
         */
        NEVER("never"),

        /**
         * colorize if the output device is a tty
         */
        AUTO("auto")
    }

    /**
     * Location of Bison
     * This property is required
     */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    val executable: RegularFileProperty

    /**
     * The grammar file to be processed
     * This property is required
     */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    val grammarFile: RegularFileProperty

    /**
     * File loction where the generated file will be written to
     * This property is required
     */
    @get:OutputFile
    val outputFile: RegularFileProperty

    /**
     * Location of an custom skeleton file UTF-8 encoded.
     * This is an optional property
     */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:Optional
    val customSkeletonFile: RegularFileProperty

    /**
     * Defines the macro YYDEBUG to 1 if it is not already defined, so that the debugging facilities are compiled.
     * Default is false
     */
    @get:Input
    val debug: Property<Boolean>

    /**
     * Pretend that %locations was specified
     * Default is false
     */
    @get:Input
    val locations: Property<Boolean>

    /**
     * prepend PREFIX to the external symbols
     * deprecated by 'define.get().add(api.prefix, {PREFIX})'
     * This property is optional
     */
    @Deprecated("deprecated by 'define.get().add(api.prefix, {PREFIX})'")
    @get:Optional
    @get:Input
    val namePrefix: Property<String>

    /**
     * Don't put any #line preprocessor commands in the parser file.
     * Default is false
     */
    @get:Input
    val noLines: Property<Boolean>

    /**
     * apply fixes to the source grammar file and exit
     * Default is false
     */
    @get:Input
    val update: Property<Boolean>

    /**
     * Pretend that %token-table was specified
     * Default is false
     */
    @get:Input
    val tokenTable: Property<Boolean>

    /**
     * Pretend that %header was specified, i.e., write an extra output file containing definitions for the token kind names defined in the grammar, as well as a few other declarations.
     * This property is optional
     */
    @get:Optional
    @get:OutputFile
    val header: RegularFileProperty

    /**
     * similar to '%define NAME VALUE'
     * Default is a empty Map
     */
    @get:Input
    val define: MapProperty<String, String>

    /**
     * override '%define NAME VALUE'
     * Default is a empty Map
     */
    @get:Input
    val forceDefine: MapProperty<String, String>

    /**
     * Specify a prefix to use for all Bison output file names.
     * This property is optional
     */
    @get:Optional
    @get:Input
    val filePrefix: Property<String>

    /**
     * Write an extra output file containing verbose description.
     * @see Report for the particular options
     * Default is an empty set
     */
    @get:Input
    val report: SetProperty<Report>

    /**
     * Specify the file for the verbose description.
     * This property is optional
     */
    @get:Optional
    @get:OutputFile
    val reportFile: RegularFileProperty

    /**
     * Pretend that %verbose was specified, i.e, write an extra output file containing verbose descriptions of the grammar and parser.
     * Default is false
     */
    @get:Input
    val verbose: Property<Boolean>

    /**
     * Output a VCG definition of the LALR(1) grammar automaton computed by Bison
     * This property is optional
     */
    @get:Optional
    @get:OutputFile
    val graphFile: RegularFileProperty

    /**
     * Output an XML report of the parser’s automaton computed by Bison.
     * This property is optional
     */
    @get:Optional
    @get:OutputFile
    val xmlReport: RegularFileProperty

    /**
     * Replace prefix old with new when writing file paths in output files.
     * Default is a empty Map
     */
    @get:Input
    val filePrefixMap: MapProperty<String, String>

    /**
     * specify the output programming language
     * This property is optional
     */
    @get:Optional
    @get:Input
    val language: Property<String>

    /**
     * Activate miscellaneous features
     * @see Features
     * Default is a empty Set
     */
    @get:Input
    val features: SetProperty<Features>

    /**
     * Report warnings falling in a certain Category
     * Default is a empty Set, however this does not interfere with Bisons internal defaults
     * @see ErrorCategory
     * Default is a empty Set
     */
    @get:Input
    val warnings: SetProperty<ErrorCategory>

    /**
     * Treat warnings falling in a certain Category as errors
     * @see ErrorCategory
     * Default is a empty Set
     */
    @get:Input
    val errors: SetProperty<ErrorCategory>

    /**
     * whether to colorize the diagnostics
     * This property is optional
     */
    @get:Input
    @get:Optional
    val color: Property<When>

    /**
     * specify the CSS FILE for colorizer diagnostics
     * This property is optional
     */
    @get:Optional
    @get:InputFile
    val style: RegularFileProperty

    /**
     * Generates a source file of the provided grammar and parameters to the provided target location
     * @throws BisonTaskError.MissingBisonExecError if no Bison Executable was given
     * @throws BisonTaskError.MissingGrammarFileError if no Grammar was given
     * @throws BisonTaskError.MissingOutputFileError if no OutputFile was given
     * @throws BisonTaskError.CodeGenerationRuntimeError if something went wrong at runtime of the code generation
     */
    @TaskAction
    @Throws(BisonTaskError::class)
    fun generate()
}
