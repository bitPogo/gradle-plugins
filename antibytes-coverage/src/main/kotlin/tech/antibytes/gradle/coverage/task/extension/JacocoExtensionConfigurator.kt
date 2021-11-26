/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.extension

import org.gradle.api.Project
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import tech.antibytes.gradle.coverage.AntiBytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.task.TaskContract

internal object JacocoExtensionConfigurator : TaskContract.JacocoExtensionConfigurator {
    override fun configure(project: Project, configuration: AntiBytesCoveragePluginExtension) {
        val extension = project.extensions.getByType(JacocoPluginExtension::class.java)

        extension.toolVersion = configuration.jacocoVersion
    }
}
