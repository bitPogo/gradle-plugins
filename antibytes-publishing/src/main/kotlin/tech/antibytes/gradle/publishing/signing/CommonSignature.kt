/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.publishing.signing

import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.plugins.signing.SigningExtension

object CommonSignature : SigningContract.CommonSignature {
    override fun configure(project: Project) {
        project.extensions.configure(SigningExtension::class.java) {
            isRequired = project.gradle.taskGraph.allTasks.any { it is PublishToMavenRepository }
        }
    }
}
