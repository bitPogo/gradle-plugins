/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import tech.antibytes.gradle.publishing.PublishingContract
import tech.antibytes.gradle.util.isRoot

internal object SigningController : SigningContract.SigningController {
    override fun configure(
        project: Project,
        extension: PublishingContract.PublishingPluginExtension,
    ) {
        if (project.isRoot()) {
            project.evaluationDependsOnChildren()
        }

        project.afterEvaluate {
            CommonSigning.configure(project)

            val signingConfig = extension.signingConfiguration ?: return@afterEvaluate
            MemorySigning.configure(project, signingConfig)
        }
    }
}
