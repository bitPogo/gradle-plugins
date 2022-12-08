/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.component

import java.io.File
import java.util.Date
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.attributes.Attribute
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.api.tasks.TaskDependency
import org.gradle.kotlin.dsl.named
import tech.antibytes.gradle.component.CustomComponentContract.Companion.EXTENSION_ID

class AntiBytesCustomComponent @Inject constructor(
    private val softwareComponentFactory: SoftwareComponentFactory,
) : Plugin<Project> {
    private class CustomArtifact(
        private val configuration: CustomComponentContract.Extension,
    ) : PublishArtifact {
        override fun getBuildDependencies(): TaskDependency = configuration.buildDependencies.get()

        override fun getName(): String = configuration.name.get()

        override fun getExtension(): String = configuration.typeExtension.get()

        override fun getType(): String = configuration.type.get()

        override fun getClassifier(): String? = configuration.classifier.orNull

        override fun getFile(): File = configuration.componentHandle.asFile.get().also {
            println(it)
        }

        override fun getDate(): Date? = configuration.date.orNull
    }

    private fun Project.createConfiguration(extension: CustomComponentContract.Extension): Configuration {
        return configurations.create(EXTENSION_ID) {
            isCanBeResolved = false
            isCanBeConsumed = true

            artifacts {
                add(EXTENSION_ID, CustomArtifact(extension))
            }
        }
    }

    private fun addAttributes(project: Project, extension: CustomComponentContract.Extension) {
        project.configurations.named(EXTENSION_ID) {
            extension.attributes.get().forEach { (attributeKey, value) ->
                attributes {
                    @Suppress("UNCHECKED_CAST")
                    attribute(attributeKey as Attribute<Any>, value)
                }
            }
        }
    }

    private fun AdhocComponentWithVariants.configure(configuration: Configuration) {
        addVariantsFromConfiguration(configuration) {
            mapToMavenScope("compile")
        }
    }

    override fun apply(target: Project) {
        val extension = target.extensions.create(EXTENSION_ID, AntibytesCustomComponentPluginExtension::class.java)

        val component = softwareComponentFactory.adhoc(EXTENSION_ID)
        target.components.add(component)

        component.configure(target.createConfiguration(extension))

        target.afterEvaluate {
            addAttributes(this, extension)
        }
    }
}
