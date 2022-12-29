/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

internal interface CoverageContract {
    interface CoveragePluginExtension {
        val jacocoVersion: Property<String>
        val appendKmpJvmTask: Property<Boolean>
        val configurations: MapProperty<String, CoverageApiContract.CoverageConfiguration>
    }

    fun interface TaskController {
        fun configure(project: Project, extension: AntiBytesCoveragePluginExtension)
    }

    fun interface DefaultConfigurationProvider {
        fun createDefaultCoverageConfiguration(project: Project): MutableMap<String, CoverageApiContract.CoverageConfiguration>
    }

    companion object CONSTANTS {
        const val EXTENSION_ID = "antibytesCoverage"
        const val DEFAULT_ANDROID_VARIANT = "debug"
        const val DEFAULT_ANDROID_FLAVOUR = ""
        val DEFAULT_ANDROID_MARKER = DEFAULT_ANDROID_FLAVOUR.capitalize() + DEFAULT_ANDROID_VARIANT.capitalize()
        val DEPENDENCIES = listOf("jacoco")
    }
}
