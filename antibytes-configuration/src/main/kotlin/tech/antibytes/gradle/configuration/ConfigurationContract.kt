/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project

internal interface ConfigurationContract {
    fun interface DefaultAndroidLibraryConfigurationProvider {
        fun createDefaultConfiguration(project: Project): AndroidConfigurationApiContract.AndroidLibraryConfiguration
    }

    fun interface DefaultAndroidApplicationConfigurationProvider {
        fun createDefaultConfiguration(project: Project): AndroidConfigurationApiContract.AndroidApplicationConfiguration
    }

    interface AndroidLibraryConfigurator {
        fun configure(
            project: Project,
            configuration: AndroidConfigurationApiContract.AndroidLibraryConfiguration,
        )
    }

    interface AndroidApplicationConfigurator {
        fun configure(
            project: Project,
            configuration: AndroidConfigurationApiContract.AndroidApplicationConfiguration,
        )
    }
}
