/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.Versioning

internal object PublisherController : PublishingContract.PublisherController {
    private fun addVersionTask(
        project: Project,
        configuration: PublishingApiContract.VersioningConfiguration
    ) {
        if (project.rootProject.tasks.findByName("versionInfo") !is Task) {
            project.rootProject.tasks.create("versionInfo") {
                group = "Versioning"
                description = "Displays the current version"

                doLast {
                    val info = Versioning.versionInfo(project, configuration)
                    println("version: ${info.name}")
                    println("last tag: ${info.details.lastTag}")
                    println("distance from last tag: ${info.details.commitDistance}")
                    println("clean: ${info.details.isCleanTag}")
                    println("hash: ${info.details.gitHashFull}")
                }
            }
        }
    }

    override fun configure(
        project: Project,
        configuration: PublishingContract.PublishingPluginConfiguration,
    ) {
        val isRoot = project.rootProject == project
        if (isRoot) {
            project.evaluationDependsOnChildren()
        }

        project.afterEvaluate {
            addVersionTask(project, configuration.versioning)

            when {
                configuration.excludeProjects.contains(project.name) -> { /* Do nothing */ }
                configuration.standalone -> PublisherStandaloneController.configure(project, configuration)
                isRoot -> PublisherRootProjectController.configure(project, configuration)
                else -> PublisherSubProjectController.configure(project, configuration)
            }
        }
    }
}
