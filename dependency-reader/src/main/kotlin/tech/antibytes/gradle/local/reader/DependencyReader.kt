/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local.reader

import com.google.gson.Gson
import java.io.File
import java.io.InputStreamReader
import java.io.Reader
import tech.antibytes.gradle.local.DependencyVersionContract.Companion.PYTHON_SEPARATOR
import tech.antibytes.gradle.local.DependencyVersionContract.Companion.TOML_COMMENTS
import tech.antibytes.gradle.local.DependencyVersionContract.Companion.TOML_SEPARATOR
import tech.antibytes.gradle.local.DependencyVersionContract.DependencyReader
import tech.antibytes.gradle.local.DependencyVersionContract.NodeDependencies
import tech.antibytes.gradle.local.DependencyVersionContract.PackageDependencies
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
    private fun String.partition(separator: String): List<String> = split(separator, limit = 2)

    @JvmStatic
    private fun String.parseLine(separator: String): Pair<String, String>? {
        val partitions = partition(separator)

        return if (partitions.size == 2) {
            partitions[0] to partitions[1].trim()
        } else {
            null
        }
    }

    @JvmStatic
    private fun String.readPythonLine(): Pair<String, String>? = parseLine(PYTHON_SEPARATOR)

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

    override fun getPythonReader(file: File): DependencyReader<Map<String, String>> = file.checkFile().readPython()

    @JvmStatic
    private fun PackageDependencies.asNodeDependencies(): NodeDependencies {
        return NodeDependencies(
            production = dependencies ?: emptyMap(),
            development = devDependencies ?: emptyMap(),
            peer = peerDependencies ?: emptyMap(),
            optional = optionalDependencies ?: emptyMap(),
        )
    }

    @JvmStatic
    private fun File.readPackageJson(): NodeDependencies {
        return Gson().fromJson(this.reader(), PackageDependencies::class.java).asNodeDependencies()
    }

    @JvmStatic
    private fun File.readNodeJs(): DependencyReader<NodeDependencies> = DependencyReader { readPackageJson() }

    override fun getNodeReader(
        file: File,
    ): DependencyReader<NodeDependencies> = file.checkFile().readNodeJs()

    @JvmStatic
    private fun File.readGradleToml(): Map<String, String> = TomlReader(this.reader()).readVersion()

    @JvmStatic
    private fun File.readGradle(): DependencyReader<Map<String, String>> = DependencyReader { readGradleToml() }

    override fun getGradleReader(
        file: File,
    ): DependencyReader<Map<String, String>> = file.checkFile().readGradle()

    private class TomlReader(private val reader: Reader) {
        private fun String.isStart(): Boolean = this.trim() == "[versions]"
        private fun String.isEnd(): Boolean = this.trim().startsWith("[")

        private fun String.removeComments(): String = this.substringBefore(TOML_COMMENTS)

        private fun MutableMap<String, String>.addVersionTable(line: String) {
            val parsed = line.removeComments().parseLine(TOML_SEPARATOR)

            if (parsed != null) {
                this[parsed.first.trim()] = parsed.second.trim('"').trim()
            }
        }

        fun readVersion(): Map<String, String> {
            val versions: MutableMap<String, String> = mutableMapOf()
            var readVersions = false

            reader.forEachLine { line ->
                when {
                    readVersions && line.isEnd() -> readVersions = false
                    !readVersions && line.isStart() -> readVersions = true
                    readVersions -> versions.addVersionTable(line)
                    else -> { /*Do Nothing */ }
                }
            }

            return versions
        }
    }
}
