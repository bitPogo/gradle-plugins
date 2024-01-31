/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract.PublishingPluginExtension
import tech.antibytes.gradle.publishing.PublishingContract.SigningController
import tech.antibytes.gradle.util.isRoot

internal object SigningController : SigningController {
    private fun PublishingPluginExtension.allowsSigning(projectName: String): Boolean {
        return !signing.isPresent && !standalone.get() && projectName !in excludeProjects.get()
    }

    private fun Project.isSignable(): Boolean {
        val extension = extensions.findByType(PublishingPluginExtension::class.java)

        return extension != null && extension.allowsSigning(name)
    }

    private fun Project.applySigningConfiguration(
        signingConfig: PublishingApiContract.MemorySigning?,
    ) {
        if (signingConfig != null) {
            CommonSignature.configure(this)
            MemorySignature.configure(this, signingConfig)
        }
    }

    private fun Project.configureSubprojects(
        extension: PublishingPluginExtension,
    ) {
        val signingConfig = extension.signing.orNull

        subprojects.forEach { subproject ->
            if (subproject.name !in extension.excludeProjects.get() && subproject.isSignable()) {
                subproject.applySigningConfiguration(signingConfig)
            }
        }
    }

    override fun configure(
        project: Project,
        extension: PublishingPluginExtension,
    ) {
        if (project.isRoot()) {
            project.evaluationDependsOnChildren()
        }

        project.afterEvaluate {
            if (project.isRoot()) {
                project.configureSubprojects(extension)
            } else {
                project.applySigningConfiguration(extension.signing.orNull)
            }
        }
    }
}
