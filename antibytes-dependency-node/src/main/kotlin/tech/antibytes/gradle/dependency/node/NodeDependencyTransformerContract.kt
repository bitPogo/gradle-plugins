/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.node

import java.io.File
import org.gradle.api.initialization.dsl.VersionCatalogBuilder

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
        fun extractPackages(file: File): NodeDependencies
    }

    fun interface Bridge {
        fun addNodeDependencies(
            builder: VersionCatalogBuilder,
            dependencies: NodeDependencies,
        )
    }
}
