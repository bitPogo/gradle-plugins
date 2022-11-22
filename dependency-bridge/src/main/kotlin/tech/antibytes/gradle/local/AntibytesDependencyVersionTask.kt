/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.local

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.local.DependencyVersionContract.DependencyVersionTask
import tech.antibytes.gradle.local.DependencyVersionContract.Reader
import tech.antibytes.gradle.local.reader.DependencyReader
import tech.antibytes.gradle.local.writer.DependencyWriter
import java.io.File

abstract class AntibytesDependencyVersionTask : DefaultTask(), DependencyVersionTask {
    @Suppress("ANNOTATION_TARGETS_NON_EXISTENT_ACCESSOR")
    @get:Internal
    private val outputDirectory: File = File(
        "${project.buildDir.absolutePath.trimEnd('/')}/generated/antibytes/main/kotlin",
    )

    init {
        packageName.convention(null)
        pythonDirectory.convention(emptyList())
        nodeDirectory.convention(emptyList())
        gradleDirectory.convention(emptyList())
    }

    private fun guardExecution() {
        if (!packageName.isPresent || packageName.get().isEmpty()) {
            throw StopExecutionException("Missing Package declaration!")
        }
    }

    private fun ensureBuildDir() {
        if (!project.buildDir.exists()) {
            project.buildDir.mkdir()
        }
    }

    private fun MutableMap<String, String>.addDependencies(
        dependencyFiles: Array<File>,
        indicator: String,
        reader: Function1<File, Reader<Map<String, String>>>,
    ) {
        dependencyFiles.forEach { file ->
            if (file.name.endsWith(indicator)) {
                putAll(reader(file).extractVersions())
            }
        }
    }

    private fun extractDependencies(
        provider: ListProperty<File>,
        indicator: String,
        reader: Function1<File, Reader<Map<String, String>>>,
    ): Map<String, String> {
        val dependencyFiles = provider.orNull
        val dependencies: MutableMap<String, String> = mutableMapOf()

        dependencyFiles?.forEach { folder ->
            dependencies.addDependencies(
                dependencyFiles = folder.listFiles()!!,
                indicator = indicator,
                reader = reader,
            )
        }

        return dependencies
    }

    private fun extractPythonDependencies(): Map<String, String> {
        return extractDependencies(
            provider = pythonDirectory,
            indicator = "requirements.txt",
        ) { file -> DependencyReader.getPythonReader(file) }
    }

    private fun extractGradleDependencies(): Map<String, String> {
        return extractDependencies(
            provider = gradleDirectory,
            indicator = ".toml"
        ) { file -> DependencyReader.getGradleReader(file) }
    }

    private fun DependencyWriter.bridgePythonDependencies() {
        val dependencies = extractPythonDependencies()
        writePythonDependencies(dependencies)
    }

    private fun DependencyWriter.bridgeGradleDependencies() {
        val dependencies = extractGradleDependencies()
        writeGradleDependencies(dependencies)
    }

    @TaskAction
    override fun generate() {
        guardExecution()
        ensureBuildDir()

        val writer = DependencyWriter(
            packageName = packageName.get(),
            outputDirectory = outputDirectory
        )

        writer.bridgePythonDependencies()
        writer.bridgeGradleDependencies()
    }
}
