/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project

internal interface CoverageContract {
    interface Extension {
        var jacocoVersion: String
        val coverageConfigurations: MutableMap<String, CoverageApiContract.CoverageConfiguration>
    }

    fun interface DefaultConfigurationProvider {
        fun createDefaultCoverageConfiguration(project: Project): MutableMap<String, CoverageApiContract.CoverageConfiguration>
    }

    fun interface TaskController {
        fun configure(project: Project, configurations: MutableMap<String, CoverageApiContract.CoverageConfiguration>)
    }

    companion object CONSTANTS {
        const val DEFAULT_ANDROID_VARIANT = "debug"
        const val DEFAULT_ANDROID_FLAVOUR = ""
        val DEFAULT_ANDROID_MARKER = DEFAULT_ANDROID_VARIANT.capitalize() + DEFAULT_ANDROID_FLAVOUR.capitalize()
    }
}
