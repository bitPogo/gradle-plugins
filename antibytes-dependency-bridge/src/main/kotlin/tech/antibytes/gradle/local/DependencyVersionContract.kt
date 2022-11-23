/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local

import java.io.File
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction

internal interface DependencyVersionContract {
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
        fun writeNodeDependencies(dependencies: NodeDependencies)
        fun writeGradleDependencies(dependencies: Map<String, String>)
    }

    interface DependencyVersionTask {
        /**
         * Namespace where the generated File lives under
         * This property is required
         */
        @get:Input
        val packageName: Property<String>

        /**
         * Directories which is used to assemble python dependencies
         * This property is required
         */
        @get:Input
        val pythonDirectory: ListProperty<File>

        /**
         * Directories which is used to assemble nodeJs dependencies
         * This property is required
         */
        @get:Input
        val nodeDirectory: ListProperty<File>

        /**
         * Directory which is used to assemble nodeJs dependencies
         * This properties is required
         */
        @get:Input
        val gradleDirectory: ListProperty<File>

        /**
         * Bridges the version files to Kotlin
         * @throws StopExecutionException if no packageName was set
         */
        @TaskAction
        fun generate()
    }

    companion object {
        const val PYTHON_SEPARATOR = "=="
        const val TOML_COMMENTS = "#"
        const val TOML_SEPARATOR = "="
    }
}
