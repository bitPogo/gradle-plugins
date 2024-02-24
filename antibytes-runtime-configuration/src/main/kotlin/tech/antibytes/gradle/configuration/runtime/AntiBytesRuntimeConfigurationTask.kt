/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration.runtime

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import kotlin.reflect.KClass
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.configuration.runtime.RuntimeConfigurationContract.RuntimeConfigurationTask
import tech.antibytes.gradle.util.capitalize

abstract class AntiBytesRuntimeConfigurationTask : DefaultTask(), RuntimeConfigurationTask {
    @get:Internal
    protected abstract val fileName: String

    @get:Internal
    protected abstract val outputDirectory: File

    init {
        configurationFilePrefix.convention(null)
        packageName.convention(null)
        sourceSetPrefix.convention(null)

        stringFields.convention(emptyMap<String, String>())
        integerFields.convention(emptyMap<String, Int>())
        longFields.convention(emptyMap<String, Long>())
        booleanFields.convention(emptyMap<String, Boolean>())
    }

    private fun addField(name: String, type: KClass<*>, value: Any): PropertySpec {
        return PropertySpec.builder(name, type, KModifier.CONST)
            .initializer(value.toString())
            .build()
    }

    private fun addFields(fields: Map<String, Any>): List<PropertySpec> {
        return fields.map { (name, raw) ->
            val value = if (raw is String) {
                "\"$raw\""
            } else {
                raw
            }

            addField(name, value::class, value)
        }
    }

    private fun addConfiguration(name: String): TypeSpec {
        val implementation = TypeSpec.objectBuilder(name)
        implementation.addModifiers(KModifier.INTERNAL)
        implementation.addProperties(addFields(stringFields.get()))
        implementation.addProperties(addFields(integerFields.get()))
        implementation.addProperties(addFields(longFields.get()))
        implementation.addProperties(addFields(booleanFields.get()))

        return implementation.build()
    }

    private fun guardExecution() {
        if (!packageName.isPresent) {
            throw StopExecutionException("Missing Package declaration!")
        }
    }

    protected fun determineSourceSet(): String = sourceSetPrefix.orNull ?: "common"

    protected fun createDirectory(target: String): File {
        val buildDir = project.layout.buildDirectory.asFile.get()

        if (!buildDir.exists()) {
            buildDir.mkdir()
        }

        return File(
            "${buildDir.absolutePath.trimEnd('/')}/generated/antibytes/$target/kotlin",
        )
    }

    private fun deterimeFilePrefix(): String = configurationFilePrefix.orNull?.capitalize() ?: ""

    @TaskAction
    override fun generate() {
        guardExecution()

        val file = FileSpec.builder(
            packageName.get(),
            "${deterimeFilePrefix()}$fileName",
        )

        file.addType(addConfiguration(fileName))

        file.build().writeTo(outputDirectory)
    }
}
