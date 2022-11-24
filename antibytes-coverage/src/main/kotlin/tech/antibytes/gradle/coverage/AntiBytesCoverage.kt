/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEPENDENCIES
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.EXTENSION_ID
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.coverage.task.TaskController
import tech.antibytes.gradle.util.applyIfNotExists
import tech.antibytes.gradle.util.isKmp

class AntiBytesCoverage : Plugin<Project> {
    private fun <T : Any, V : Any> MapProperty<T, V>.putIfAbsent(key: T, value: V) {
        val map = this.orNull

        if (map != null && !map.containsKey(key)) {
            this.put(key, value)
        }
    }

    override fun apply(target: Project) {
        val extension = target.extensions.create(
            EXTENSION_ID,
            AntiBytesCoveragePluginExtension::class.java,
            target,
        )

        target.applyIfNotExists(*DEPENDENCIES.toTypedArray())
        target.evaluationDependsOnChildren()

        target.afterEvaluate {
            if (target.isKmp() && extension.appendKmpJvmTask.get()) {
                DefaultConfigurationProvider.createDefaultCoverageConfiguration(target)["jvm"]?.also {
                    extension.configurations.putIfAbsent("jvm", it)
                }
            }

            TaskController.configure(target, extension)
        }
    }
}
