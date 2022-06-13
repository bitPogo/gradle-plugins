/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.jvm.tasks.Jar
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.util.isRoot
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.VersioningContract

internal object PublisherController : PublishingContract.PublisherController {
    private fun Project.hasTask(
        taskName: String
    ): Boolean = tasks.findByName(taskName) is Task

    private fun Project.addVersionTask(
        configuration: VersioningContract.VersioningConfiguration
    ) {
        if (!rootProject.hasTask("versionInfo")) {
            rootProject.tasks.create("versionInfo") {
                group = "Versioning"
                description = "Displays the current version"

                doLast {
                    val info = Versioning.getInstance(project, configuration).versionInfo()
                    println("version: ${info.name}")
                    println("last tag: ${info.details.lastTag}")
                    println("distance from last tag: ${info.details.commitDistance}")
                    println("clean: ${info.details.isCleanTag}")
                    println("hash: ${info.details.gitHashFull}")
                }
            }
        }
    }

    private fun Project.setVersionToProject(version: String) {
        this.version = version
    }

    private fun Project.createDocumentationTask(
        configuration: PublishingApiContract.DocumentationConfiguration
    ): Task {
        return if (!hasTask("javadoc")) {
            tasks.create("javadoc", Jar::class.java) {
                group = "Documentation"
                description = "Generates the JavaDocs"
                setDependsOn(configuration.tasks)
                archiveClassifier.set("javadoc")
                from(configuration.outputDir.absolutePath)
            }
        } else {
            tasks.getByName("javadoc")
        }
    }

    private fun Project.addDocumentationTask(
        configuration: PublishingApiContract.DocumentationConfiguration?
    ): Task? {
        return if (configuration == null) {
            null
        } else {
            createDocumentationTask(configuration)
        }
    }

    override fun configure(
        project: Project,
        version: String,
        documentation: Task?,
        extension: PublishingContract.PublishingPluginExtension,
    ) {
        if (project.isRoot()) {
            project.evaluationDependsOnChildren()
        }

        project.afterEvaluate {
            val derivedVersion = Versioning.getInstance(project, extension.versioning).versionName()
            val docTask = project.addDocumentationTask(extension.documentation)

            project.addVersionTask(extension.versioning)
            project.setVersionToProject(derivedVersion)

            when {
                extension.excludeProjects.contains(project.name) -> { /* Do nothing */
                }
                extension.standalone -> {
                    PublisherStandaloneController.configure(
                        project = project,
                        version = derivedVersion,
                        documentation = docTask,
                        extension = extension,
                    )
                }
                project.isRoot() -> {
                    PublisherRootProjectController.configure(
                        project = project,
                        version = derivedVersion,
                        documentation = docTask,
                        extension = extension,
                    )
                }
                else -> {
                    PublisherSubProjectController.configure(
                        project = project,
                        version = derivedVersion,
                        documentation = docTask,
                        extension = extension,
                    )
                }
            }
        }
    }
}
