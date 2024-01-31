/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.configuration

import java.io.File
import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEFAULT_ANDROID_FLAVOUR
import tech.antibytes.gradle.coverage.CoverageContract.CONSTANTS.DEFAULT_ANDROID_VARIANT
import tech.antibytes.gradle.util.GradleUtilApiContract

internal interface ConfigurationContract {

    data class SourceContainer(
        val platform: File,
        val common: File? = null,
    )

    fun interface SourceHelper {
        fun resolveSources(project: Project, context: GradleUtilApiContract.PlatformContext): Set<File>
    }

    fun interface DefaultJvmConfigurationProvider {
        fun createDefaultCoverageConfiguration(
            project: Project,
            context: GradleUtilApiContract.PlatformContext,
        ): CoverageApiContract.CoverageConfiguration
    }

    interface DefaultAndroidConfigurationProvider {
        fun createDefaultCoverageConfiguration(
            project: Project,
            context: GradleUtilApiContract.PlatformContext,
            variant: String = DEFAULT_ANDROID_VARIANT,
            flavour: String = DEFAULT_ANDROID_FLAVOUR,
        ): CoverageApiContract.CoverageConfiguration
    }
}
