/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar

import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.grammar.PostConverterTaskError.Companion.MISSING_TARGET_FILE

abstract class PostConverterTask : DefaultTask(), PostConverterTaskContract {
    init {
        replaceWithString.convention(emptyList())
        replaceWithRegEx.convention(emptyList())

        deleteWithString.convention(emptyList())
        deleteWithRegEx.convention(emptyList())
    }

    private fun replace(fileValue: String): String {
        var modifiedValue = fileValue

        replaceWithString.get().forEach { (old, new) ->
            modifiedValue = modifiedValue.replace(old, new)
        }

        replaceWithRegEx.get().forEach { (old, new) ->
            modifiedValue = modifiedValue.replace(old, new)
        }

        deleteWithString.get().forEach { string ->
            modifiedValue = modifiedValue.replace(string, "")
        }

        deleteWithRegEx.get().forEach { pattern ->
            modifiedValue = modifiedValue.replace(pattern, "")
        }

        return modifiedValue
    }

    @TaskAction
    override fun cleanUp() {
        val logger = Logging.getLogger(PostConverterTask::class.java)

        if (!targetFile.isPresent) {
            logger.error(MISSING_TARGET_FILE)
            throw PostConverterTaskError.TargetFileNotFound()
        } else {
            var fileValue = targetFile.get().asFile.readText()

            fileValue = replace(fileValue)

            targetFile.get().asFile.writeText(fileValue)
        }
    }
}
