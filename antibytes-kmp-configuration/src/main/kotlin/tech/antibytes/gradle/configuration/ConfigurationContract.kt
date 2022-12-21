/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project

internal interface ConfigurationContract {
    fun interface DefaultConfigurationProvider<T : Any> {
        fun createDefaultConfiguration(project: Project): T
    }

    fun interface Configurator<T : Any> {
        fun configure(
            project: Project,
            configuration: T,
        )
    }
}
