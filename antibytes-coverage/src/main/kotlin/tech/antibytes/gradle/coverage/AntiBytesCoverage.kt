/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.coverage.task.TaskController

class AntiBytesCoverage : Plugin<Project> {
    override fun apply(target: Project) {
        if (target.plugins.findPlugin("jacoco") == null) {
            target.plugins.apply("jacoco")
        }

        val extension = target.extensions.create(
            "antiBytesCoverage",
            AntiBytesCoverageExtension::class.java
        )

        target.evaluationDependsOnChildren()

        target.afterEvaluate {
            TaskController.configure(target, extension)
        }
    }
}
