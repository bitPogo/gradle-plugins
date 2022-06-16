/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingApiContract
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.util.isRoot

internal object SigningController : SigningContract.SigningController {
    private fun Project.applySigningConfiguration(
        signingConfig: PublishingApiContract.MemorySigning?
    ) {
        if (signingConfig != null) {
            CommonSigning.configure(this)
            MemorySigning.configure(this, signingConfig)
        }
    }

    private fun Project.configureSubprojects(
        signingConfig: PublishingApiContract.MemorySigning?
    ) {
        subprojects.forEach { subproject ->
            subproject.applySigningConfiguration(signingConfig)
        }
    }

    override fun configure(
        project: Project,
        extension: PublishingContract.PublishingPluginExtension,
    ) {
        if (project.isRoot()) {
            project.evaluationDependsOnChildren()
        }

        project.afterEvaluate {
            val signingConfig = extension.signingConfiguration

            if (project.isRoot()) {
                project.configureSubprojects(signingConfig)
            } else {
                project.applySigningConfiguration(signingConfig)
            }
        }
    }
}
