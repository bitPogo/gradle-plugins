/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project

internal interface ConfigurationContract {
    fun interface DefaultAndroidLibraryConfigurationProvider {
        fun createDefaultConfiguration(project: Project): ConfigurationApiContract.AndroidLibraryConfiguration
    }

    fun interface DefaultAndroidApplicationConfigurationProvider {
        fun createDefaultConfiguration(project: Project): ConfigurationApiContract.AndroidApplicationConfiguration
    }

    interface AndroidLibraryConfigurator {
        fun configure(
            project: Project,
            configuration: ConfigurationApiContract.AndroidLibraryConfiguration
        )
    }

    interface AndroidApplicationConfigurator {
        fun configure(
            project: Project,
            configuration: ConfigurationApiContract.AndroidApplicationConfiguration
        )
    }
}
