/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage

import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEPENDENCIES
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.EXTENSION_ID
import tech.antibytes.gradle.coverage.configuration.DefaultConfigurationProvider
import tech.antibytes.gradle.coverage.task.TaskController
import tech.antibytes.gradle.util.applyIfNotExists
import tech.antibytes.gradle.util.isKmp
import tech.antibytes.gradle.util.isRoot

class AntibytesCoverage internal constructor(
    private val configurationProvider: CoverageContract.DefaultConfigurationProvider,
    private val controller: CoverageContract.TaskController,
) : Plugin<Project> {
    @Suppress("unused")
    @Inject
    constructor() : this(DefaultConfigurationProvider(), TaskController())

    private fun Project.isApplicableRoot(): Boolean = isRoot() && subprojects.isNotEmpty()

    private fun <T : Any, V : Any> MapProperty<T, V>.putIfAbsent(key: T, value: V) {
        val map = this.orNull

        if (map?.containsKey(key) == false) {
            this.put(key, value)
        }
    }

    override fun apply(target: Project) {
        val extension = target.extensions.create(
            EXTENSION_ID,
            AntibytesCoveragePluginExtension::class.java,
            target,
        )

        target.applyIfNotExists(*DEPENDENCIES.toTypedArray())

        if (target.isApplicableRoot()) {
            target.allprojects.filter { it != target }.forEach { project ->
                target.evaluationDependsOn(project.path)
            }
        }

        target.afterEvaluate {
            if (target.isKmp() && extension.appendKmpJvmTask.get()) {
                configurationProvider.createDefaultCoverageConfiguration(target)["jvm"]?.also {
                    extension.configurations.putIfAbsent("jvm", it)
                }
            }

            controller.configure(target, extension)
        }
    }
}
