/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.util.GradleUtilApiContract
import java.io.File

internal interface ConfigurationContract {

    data class SourceContainer(
        val platform: File,
        val common: File? = null,
    )

    fun interface SourceHelper {
        fun resolveSources(project: Project, context: GradleUtilApiContract.PlatformContext): Set<File>
    }

    fun interface DefaultPlatformConfigurationProvider {
        fun createDefaultCoverageConfiguration(
            project: Project,
            context: GradleUtilApiContract.PlatformContext
        ): CoverageApiContract.CoverageConfiguration
    }
}
