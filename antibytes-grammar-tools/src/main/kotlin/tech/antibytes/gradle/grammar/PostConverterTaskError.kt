/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar

import org.gradle.api.tasks.StopActionException

sealed class PostConverterTaskError(
    message: String
) : StopActionException(message) {
    class TargetFileNotFound : PostConverterTaskError(MISSING_TARGET_FILE)

    companion object {
        const val MISSING_TARGET_FILE = "There was no file provided to process."
    }
}
