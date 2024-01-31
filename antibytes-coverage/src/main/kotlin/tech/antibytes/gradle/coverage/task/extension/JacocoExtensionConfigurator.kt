/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.task.extension

import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import tech.antibytes.gradle.coverage.AntibytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.task.TaskContract

internal class JacocoExtensionConfigurator : TaskContract.JacocoExtensionConfigurator {
    override fun configure(project: Project, configuration: AntibytesCoveragePluginExtension) {
        project.extensions.getByType(JacocoPluginExtension::class.java).apply {
            toolVersion = configuration.jacocoVersion.get()
        }
    }
}
