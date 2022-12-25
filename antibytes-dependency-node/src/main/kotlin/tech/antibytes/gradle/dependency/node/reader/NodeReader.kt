/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.node.reader

import org.gradle.internal.impldep.com.google.gson.Gson
import tech.antibytes.gradle.dependency.node.NodeDependencyTransformerContract.ReaderFactory
import tech.antibytes.gradle.dependency.node.NodeDependencyTransformerContract.Reader
import tech.antibytes.gradle.dependency.node.NodeDependencyTransformerContract.NodeDependencies
import tech.antibytes.gradle.dependency.node.NodeDependencyTransformerContract.PackageDependencies
import java.io.File

internal class NodeReader private constructor(
    private val nodePackages: File
) : Reader {

    override fun extractPackages(): NodeDependencies = nodePackages.readPackageJson()

    companion object : ReaderFactory {
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

        override fun getInstance(file: File): Reader = NodeReader(file.checkFile())
    }
}
