/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.publisher

import org.gradle.api.Project
import org.gradle.api.Task
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.publishing.doc.DocumentationController
import tech.antibytes.gradle.publishing.signing.SigningController
import tech.antibytes.gradle.publishing.version.VersionController
import tech.antibytes.gradle.util.isRoot

internal object PublisherController : PublishingContract.PublisherController {
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
            val derivedVersion = VersionController.configure(project, extension)
            val docTask = DocumentationController.configure(project, extension)

            when {
                extension.excludeProjects.get().contains(project.name) -> { /* Do nothing */ }
                extension.standalone.get() -> {
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

            SigningController.configure(project, extension)
        }
    }
}
