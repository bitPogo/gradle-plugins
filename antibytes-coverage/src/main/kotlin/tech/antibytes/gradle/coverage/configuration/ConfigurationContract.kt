/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import java.io.File

internal interface ConfigurationContract {
    enum class PlatformContext(val prefix: String) {
        ANDROID_APPLICATION("android"),
        ANDROID_APPLICATION_KMP("android"),
        ANDROID_LIBRARY("android"),
        ANDROID_LIBRARY_KMP("android"),
        JVM("jvm"),
        JVM_KMP("jvm"),
        UNKNOWN("")
    }

    interface PlatformContextResolver {
        fun getType(project: Project): PlatformContext
        fun isKmp(context: PlatformContext): Boolean
    }

    data class SourceContainer(
        val platform: File,
        val common: File? = null,
    )

    fun interface SourceHelper {
        fun resolveSources(project: Project, context: PlatformContext): Set<File>
    }

    interface DefaultPlatformConfigurationProvider {
        fun createDefaultCoverageConfiguration(
            project: Project,
            context: PlatformContext
        ): CoverageApiContract.CoverageConfiguration
    }
}
