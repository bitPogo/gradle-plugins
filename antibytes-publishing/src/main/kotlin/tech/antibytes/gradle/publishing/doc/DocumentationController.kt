/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.doc

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.extra
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.PublishingApiContract.DocumentationConfiguration
import tech.antibytes.gradle.util.isRoot
import java.io.File

internal object DocumentationController : PublisherContract.DocumentationController {
    private const val DOC_TASK_NAME = "javadoc"

    private fun Project.createDocumentationTask(
        configuration: DocumentationConfiguration
    ): Task {
        return tasks.create(DOC_TASK_NAME, Jar::class.java).apply {
            group = "Documentation"
            description = "Generates the JavaDocs"
            setDependsOn(configuration.tasks)
            archiveClassifier.set(DOC_TASK_NAME)
            from(configuration.outputDir.absolutePath)
        }
    }

    private fun DocumentationConfiguration.determineDocumentationTask(
        project: Project
    ): Task = project.tasks.findByName(DOC_TASK_NAME) ?: project.createDocumentationTask(this)

    private fun PublishingPluginExtension.determineDocumentationTask(
        project: Project
    ): Task? = documentation.orNull?.determineDocumentationTask(project)

    private fun Project.executeIfNotRoot(action: Function0<Task?>): Task? {
        return if (isRoot()) {
            null
        } else {
            action()
        }
    }

    override fun configure(
        project: Project,
        configuration: PublishingPluginExtension
    ): Task? = project.executeIfNotRoot { configuration.determineDocumentationTask(project) }
}
