/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.gradle.api.Plugin
import org.gradle.api.Project

class AntiBytesDependency : Plugin<Project> {
    private val plugins = listOf(
        "com.github.ben-manes.versions",
        "org.owasp.dependencycheck",
        "com.diffplug.spotless"
    )

    private fun applyDependencyPlugins(project: Project) {
        plugins.forEach { pluginName ->
            if (!project.plugins.hasPlugin(pluginName)) {
                project.plugins.apply(pluginName)
            }
        }
    }

    override fun apply(target: Project) {
        val extension = target.extensions.create(
            "antiBytesDependency",
            AntiBytesDependencyExtension::class.java
        )

        applyDependencyPlugins(target)

        DependencyUpdate.configure(target, extension)
    }
}
