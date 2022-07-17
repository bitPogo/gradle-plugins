/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar.bison

import org.gradle.api.tasks.StopActionException

sealed class BisonTaskError(
    message: String,
) : StopActionException(message) {
    class MissingBisonExecError : BisonTaskError(MISSING_BISON_EXEC)
    class MissingGrammarFileError : BisonTaskError(MISSING_GRAMMAR_FILE)
    class MissingOutputFileError : BisonTaskError(MISSING_OUTPUT_FILE)
    class CodeGenerationRuntimeError(message: String?) : BisonTaskError("$CODE_GENERATION_RUNTIME_ERROR\n$message")

    companion object {
        const val MISSING_BISON_EXEC = "There was no Bison executable provided."
        const val MISSING_GRAMMAR_FILE = "There was no grammar file provided for Bison to process."
        const val MISSING_OUTPUT_FILE = "There was no output file provided for Bison to write into."
        const val CODE_GENERATION_RUNTIME_ERROR = "Something went wrong during code generation:"
    }
}
