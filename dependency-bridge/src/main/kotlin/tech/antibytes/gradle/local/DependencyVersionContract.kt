/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local

import java.io.File

internal interface DependencyVersionContract {
    data class PackageDependencies(
        val dependencies: Map<String, String>?,
        val devDependencies: Map<String, String>?,
        val peerDependencies: Map<String, String>?,
        val optionalDependencies: Map<String, String>?,
    )

    data class NodeDependencies(
        val production: Map<String, String>,
        val development: Map<String, String>,
        val peer: Map<String, String>,
        val optional: Map<String, String>,
    )

    fun interface Reader<T : Any> {
        fun extractVersions(): T
    }

    interface ReaderFactory {
        fun getPythonReader(file: File): Reader<Map<String, String>>
        fun getNodeReader(file: File): Reader<NodeDependencies>
        fun getGradleReader(file: File): Reader<Map<String, String>>
    }

    interface Writer {
        fun writePythonDependencies(dependencies: Map<String, String>)
        fun writeNodeDependency(dependencies: NodeDependencies)
        fun writeGradleDependency(dependencies: Map<String, String>)
    }

    companion object {
        const val PYTHON_SEPARATOR = "=="
        const val TOML_COMMENTS = "##"
        const val TOML_SEPARATOR = "="
    }
}
