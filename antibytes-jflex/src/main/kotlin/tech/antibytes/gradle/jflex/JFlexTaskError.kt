/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.jflex

import org.gradle.api.tasks.StopActionException

sealed class JFlexTaskError(
    message: String
) : StopActionException(message) {
    class MissingFlexFileError : JFlexTaskError(MISSING_FLEX_FILE)
    class MissingOutputDirectoryError : JFlexTaskError(MISSING_OUTPUT_DIRECTORY)
    class CodeGenerationRuntimeError(message: String?) : JFlexTaskError("${CODE_GENERATION_RUNTIME_ERROR}\n$message")

    companion object {
        const val MISSING_FLEX_FILE = "There was no file provided for JFlex to process."
        const val MISSING_OUTPUT_DIRECTORY = "There was no output directory provided for JFlex to write into."
        const val CODE_GENERATION_RUNTIME_ERROR = "Something went wrong during code generation:"
    }
}
