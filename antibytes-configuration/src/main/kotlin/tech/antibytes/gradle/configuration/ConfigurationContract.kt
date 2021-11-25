/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project

internal interface ConfigurationContract {
    interface ConfigurationPluginExtension {
        val android: ConfigurationApiContract.AndroidBaseConfiguration?
    }

    fun interface DefaultAndroidLibraryConfigurationProvider {
        fun createDefaultConfiguration(project: Project): ConfigurationApiContract.AndroidLibraryConfiguration?
    }

    fun interface AndroidLibraryConfigurator {
        fun configure(
            project: Project,
            configuration: ConfigurationApiContract.AndroidLibraryConfiguration
        )
    }

    companion object {
        const val EXTENSION_ID = "antibytesProjectConfiguration"
    }
}
