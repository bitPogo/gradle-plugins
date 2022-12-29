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
import tech.antibytes.gradle.component.CustomComponentContract.Companion.EXTENSION_ID

class AntibytesCustomComponent @Inject constructor(
    private val softwareComponentFactory: SoftwareComponentFactory,
) : Plugin<Project> {
    private class CustomArtifact(
        private val artifact: CustomComponentApiContract.Artifact,
    ) : PublishArtifact {
        override fun getBuildDependencies(): TaskDependency = artifact.buildDependencies

        override fun getName(): String = artifact.name

        override fun getExtension(): String = artifact.typeExtension

        override fun getType(): String = artifact.type

        override fun getClassifier(): String? = artifact.classifier

        override fun getFile(): File = artifact.componentHandle

        override fun getDate(): Date? = artifact.date
    }

    private fun Project.createConfiguration(extension: CustomComponentContract.Extension): Configuration {
        return configurations.create(EXTENSION_ID).apply {
            isCanBeResolved = false
            isCanBeConsumed = true

            extension.customArtifacts.get().forEach { artifact ->
                artifacts.add(CustomArtifact(artifact))
            }
        }
    }

    private fun Project.addAttributes(extension: CustomComponentContract.Extension) {
        configurations.named(EXTENSION_ID).get().apply {
            extension.attributes.get().forEach { (attributeKey, value) ->
                attributes.apply {
                    @Suppress("UNCHECKED_CAST")
                    attribute(attributeKey as Attribute<Any>, value)
                }
            }
        }
    }

    private fun AdhocComponentWithVariants.configure(
        configuration: Configuration,
        extension: CustomComponentContract.Extension,
    ) {
        addVariantsFromConfiguration(configuration) {
            mapToMavenScope(extension.scope.get())
        }
    }

    override fun apply(target: Project) {
        val extension = target.extensions.create(EXTENSION_ID, AntibytesCustomComponentExtension::class.java)

        val component = softwareComponentFactory.adhoc(EXTENSION_ID)
        target.components.add(component)

        target.afterEvaluate {
            component.configure(
                target.createConfiguration(extension),
                extension,
            )
            addAttributes(extension)
        }
    }
}
