/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.antibytes.gradle.configuration.ConfigurationContract.Companion.EXTENSION_ID

class AntiBytesConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create(
            EXTENSION_ID,
            AntiBytesConfigurationPluginExtension::class.java,
            target
        )

        target.afterEvaluate {
            if (extension.android is ConfigurationApiContract.AndroidLibraryConfiguration) {
                AndroidLibraryConfigurator.configure(
                    target,
                    extension.android as ConfigurationApiContract.AndroidLibraryConfiguration
                )
            }
        }
    }
}
