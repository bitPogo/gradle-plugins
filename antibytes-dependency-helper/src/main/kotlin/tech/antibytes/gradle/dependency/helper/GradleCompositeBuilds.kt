/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import org.gradle.api.Project

internal object GradleCompositeBuilds : DependencyContract.Configurator {
    private fun Project.configureClean() {
        tasks.named("clean") {
            gradle.includedBuilds.forEach { project ->
                dependsOn(gradle.includedBuild(project.name).task(":clean"))
            }
        }
    }

    override fun configure(project: Project) {
        project.configureClean()
    }
}
