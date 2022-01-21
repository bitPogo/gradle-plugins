/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project

internal interface CoverageContract {
    interface CoveragePluginExtension {
        var jacocoVersion: String
        var appendKmpJvmTask: Boolean
        val configurations: MutableMap<String, CoverageApiContract.CoverageConfiguration>
    }

    fun interface TaskController {
        fun configure(project: Project, extension: AntiBytesCoveragePluginExtension)
    }

    fun interface DefaultConfigurationProvider {
        fun createDefaultCoverageConfiguration(project: Project): MutableMap<String, CoverageApiContract.CoverageConfiguration>
    }

    companion object CONSTANTS {
        const val EXTENSION_ID = "antiBytesCoverage"
        const val JACOCO_VERSION = "0.8.7"
        const val DEFAULT_ANDROID_VARIANT = "debug"
        const val DEFAULT_ANDROID_FLAVOUR = ""
        val DEFAULT_ANDROID_MARKER = DEFAULT_ANDROID_FLAVOUR.capitalize() + DEFAULT_ANDROID_VARIANT.capitalize()
        val DEPENDENCIES = listOf("jacoco")
    }
}
