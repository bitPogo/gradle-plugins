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

    fun interface DependencyReader<T : Any> {
        fun extractVersions(): T
    }

    interface ReaderFactory {
        fun getPythonReader(file: File): DependencyReader<Map<String, String>>
        fun getNodeReader(file: File): DependencyReader<NodeDependencies>
    }

    companion object {
        const val PYTHON_SEPARATOR = "=="
    }
}
