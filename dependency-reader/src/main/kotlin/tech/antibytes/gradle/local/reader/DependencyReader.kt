/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local.reader

import java.io.File
import java.io.InputStreamReader
import tech.antibytes.gradle.local.DependencyVersionContract.Companion.PYTHON_SEPARATOR
import tech.antibytes.gradle.local.DependencyVersionContract.DependencyReader
import tech.antibytes.gradle.local.DependencyVersionContract.ReaderFactory

internal object DependencyReader : ReaderFactory {
    @JvmStatic
    private fun File.guard(
        errorMessage: String,
        condition: File.() -> Boolean,
    ) {
        if (condition()) {
            throw IllegalArgumentException(errorMessage)
        }
    }

    @JvmStatic
    private fun File.guardExistence() = guard("The given file does not exists.") { !exists() }

    @JvmStatic
    private fun File.guardType() = guard("The given file is not a file.") { !isFile }

    @JvmStatic
    private fun File.guardReadability() = guard("The given file is not readable.") { !canRead() }

    @JvmStatic
    private fun File.checkFile(): File {
        guardExistence()
        guardType()
        guardReadability()
        return this
    }

    @JvmStatic
    private fun String.partition(): List<String> = split(PYTHON_SEPARATOR, limit = 2)

    @JvmStatic
    private fun String.readPythonLine(): Pair<String, String>? {
        val partitions = partition()

        return if (partitions.size == 2) {
            partitions[0] to partitions[1]
        } else {
            null
        }
    }

    @JvmStatic
    private fun InputStreamReader.readPythonLines(): Map<String, String> {
        val versions: MutableList<Pair<String, String>> = mutableListOf()

        forEachLine { line ->
            val version = line.readPythonLine()
            if (version != null) {
                versions.add(version)
            }
        }

        return versions.toMap()
    }

    @JvmStatic
    private fun File.readPython(): DependencyReader<Map<String, String>> {
        return DependencyReader { this.reader().readPythonLines() }
    }

    override fun getPythonReader(
        file: File,
    ): DependencyReader<Map<String, String>> = file.checkFile().readPython()
}
