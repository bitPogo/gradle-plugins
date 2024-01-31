/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.dependency.helper

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.dependency.helper.DependencyContract.Companion.DEPENDENCIES
import tech.antibytes.gradle.dependency.helper.DependencyContract.Companion.EXTENSION_ID
import tech.antibytes.gradle.util.applyIfNotExists

class AntibytesDependencyHelper : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create(
            EXTENSION_ID,
            AntibytesDependencyPluginExtension::class.java,
        )

        target.applyIfNotExists(*DEPENDENCIES.toTypedArray())

        DependencyUpdate.configure(target, extension)
    }
}
