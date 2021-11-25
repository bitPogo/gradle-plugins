/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project

abstract class AntiBytesConfigurationPluginExtension(project: Project) : ConfigurationContract.ConfigurationPluginExtension {
    override val android: ConfigurationApiContract.AndroidBaseConfiguration? = DefaultAndroidLibraryConfigurationProvider.createDefaultConfiguration(project)
}
