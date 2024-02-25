/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.local.writer

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import tech.antibytes.gradle.local.DependencyVersionContract.NodeDependencies
import tech.antibytes.gradle.local.DependencyVersionContract.Writer

internal class DependencyWriter(
    private val packageName: String,
    private val outputDirectory: File,
) : Writer {
    private fun intoFile(fileName: String, action: () -> TypeSpec) {
        val file = FileSpec.builder(
            packageName,
            fileName,
        )

        file.addType(action())

        file.build().writeTo(outputDirectory)
    }

    private fun buildProperty(name: String, value: String): PropertySpec {
        return PropertySpec.builder(name, String::class, KModifier.CONST, KModifier.INTERNAL)
            .initializer("\"$value\"")
            .build()
    }

    private fun TypeSpec.Builder.addProperty(name: String, value: String) = addProperty(buildProperty(name, value))

    private fun String.toPropertyName(): String {
        return this.split('-', '.', '/', '@')
            .joinToString("") { part -> part.capitalize() }
            .decapitalize()
    }

    private fun TypeSpec.Builder.addProperties(dependencies: Map<String, String>) {
        dependencies.forEach { (module, version) ->
            addProperty(
                module.toPropertyName(),
                version,
            )
        }
    }

    private fun writeDependency(
        name: String,
        dependencies: Map<String, String>,
    ) {
        if (dependencies.isNotEmpty()) {
            intoFile(name) {
                val implementation = TypeSpec.objectBuilder(name)
                implementation.addModifiers(KModifier.INTERNAL)
                implementation.addProperties(dependencies)

                implementation.build()
            }
        }
    }

    override fun writePythonDependencies(
        dependencies: Map<String, String>,
    ) = writeDependency("PythonVersions", dependencies)

    override fun writeNodeDependencies(dependencies: NodeDependencies) {
        writeDependency("NodeProductionVersions", dependencies.production)
        writeDependency("NodeDevelopmentVersions", dependencies.development)
        writeDependency("NodePeerVersions", dependencies.peer)
        writeDependency("NodeOptionalVersions", dependencies.optional)
    }

    override fun writeGradleDependencies(
        dependencies: Map<String, String>,
    ) = writeDependency("GradleVersions", dependencies)
}
