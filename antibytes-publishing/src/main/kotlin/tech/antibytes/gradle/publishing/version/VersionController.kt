/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.version

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.publisher.PublisherContract
import tech.antibytes.gradle.publishing.publisher.Version
import tech.antibytes.gradle.versioning.Versioning
import tech.antibytes.gradle.versioning.VersioningContract

internal object VersionController : PublisherContract.VersionController {
    private fun Project.hasTask(
        taskName: String,
    ): Boolean = tasks.findByName(taskName) is Task

    private fun Project.addVersionTask(
        configuration: VersioningContract.VersioningConfiguration,
    ) {
        if (!hasTask("versionInfo")) {
            tasks.create("versionInfo") {
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

    override fun configure(
        project: Project,
        configuration: PublishingPluginExtension
    ): Version {
        val derivedVersion = Versioning.getInstance(
            project,
            configuration.versioning.get()
        ).versionName()

        project.addVersionTask(configuration.versioning.get())
        project.setVersionToProject(derivedVersion)

        return derivedVersion
    }
}
