/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.grammar

import org.gradle.api.Task
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

interface PostConverterTaskContract : Task {
    /**
     * The file to be processed
     * This property is required
     */
    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    val targetFile: RegularFileProperty

    /**
     * Replacements which rely on plain Strings
     * The default is emptyList
     */
    @get:Input
    val replaceWithString: ListProperty<Pair<String, String>>

    /**
     * Replacements which rely on RegularExpressions
     * The default is emptyList
     */
    @get:Input
    val replaceWithRegEx: ListProperty<Pair<Regex, String>>

    /**
     * Deletions which rely on plain Strings
     * The default is emptyList
     */
    @get:Input
    val deleteWithString: ListProperty<String>

    /**
     * Deletions which rely on RegularExpressions
     * The default is emptyList
     */
    @get:Input
    val deleteWithRegEx: ListProperty<Regex>

    /**
     * Cleans the provided target with the given provided up
     * @throws TargetFileNotFound if no valid target file was not provided
     */
    @TaskAction
    fun cleanUp()
}
