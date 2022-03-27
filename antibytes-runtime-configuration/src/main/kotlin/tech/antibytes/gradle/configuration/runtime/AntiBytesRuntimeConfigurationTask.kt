/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration.runtime

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction
import tech.antibytes.gradle.configuration.runtime.RuntimeConfigurationContract.RuntimeConfigurationTask
import java.io.File
import kotlin.reflect.KClass

abstract class AntiBytesRuntimeConfigurationTask : DefaultTask(), RuntimeConfigurationTask {
    @get:Internal
    protected abstract val fileName: String
    @get:Internal
    protected abstract val outputDirectory: File

    init {
        packageName.convention(null)

        stringFields.convention(emptyMap<String, String>())
        integerFields.convention(emptyMap<String, Int>())
    }

    private fun addField(name: String, type: KClass<*>, value: Any): PropertySpec {
        return PropertySpec.builder(name, type)
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

        return implementation.build()
    }

    private fun guardExecution() {
        if (!packageName.isPresent) {
            throw StopExecutionException("Missing Package declaration!")
        }
    }

    @TaskAction
    override fun generate() {
        guardExecution()

        val file = FileSpec.builder(
            packageName.get(),
            fileName
        )

        file.addType(addConfiguration(fileName))

        file.build().writeTo(outputDirectory)
    }
}
