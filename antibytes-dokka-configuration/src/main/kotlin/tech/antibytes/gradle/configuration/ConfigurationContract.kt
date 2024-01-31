/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.configuration

import org.gradle.api.Project

internal interface ConfigurationContract {
    fun interface Configurator<T : Any> {
        fun configure(
            project: Project,
            configuration: T,
        )
    }
}
