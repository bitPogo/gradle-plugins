/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.node

import java.io.File

internal interface NodeDependencyTransformerContract {
    data class PackageDependencies(
        val dependencies: Map<String, String>?,
        val devDependencies: Map<String, String>?,
        val peerDependencies: Map<String, String>?,
        val optionalDependencies: Map<String, String>?,
    )

    data class NodeDependencies(
        val production: Map<String, String> = emptyMap(),
        val development: Map<String, String> = emptyMap(),
        val peer: Map<String, String> = emptyMap(),
        val optional: Map<String, String> = emptyMap(),
    )

    fun interface Reader {
        fun extractPackages(): NodeDependencies
    }

    fun interface ReaderFactory {
        fun getInstance(file: File): Reader
    }
}
