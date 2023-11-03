/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.configuration.value.AndroidConfigurationProvider
import tech.antibytes.gradle.coverage.configuration.value.JvmConfigurationProvider
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.PlatformContextResolver

internal class DefaultConfigurationProvider(
    private val androidConfigurationProvider: ConfigurationContract.DefaultAndroidConfigurationProvider = AndroidConfigurationProvider(),
    private val jvmConfigurationProvider: ConfigurationContract.DefaultJvmConfigurationProvider = JvmConfigurationProvider(),
) : CoverageContract.DefaultConfigurationProvider {
    private fun fetchConfiguration(
        project: Project,
        context: PlatformContext,
    ): CoverageApiContract.CoverageConfiguration {
        return if (context.prefix == "jvm") {
            jvmConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                context,
            )
        } else {
            androidConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                context,
            )
        }
    }

    override fun createDefaultCoverageConfiguration(project: Project): MutableMap<String, CoverageApiContract.CoverageConfiguration> {
        val contexts = PlatformContextResolver.getType(project)
        val configurations: MutableMap<String, CoverageApiContract.CoverageConfiguration> = mutableMapOf()

        contexts.forEach { context ->
            configurations[context.prefix] = fetchConfiguration(project, context)
        }

        return configurations
    }
}
