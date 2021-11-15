/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.CoverageContract
import tech.antibytes.gradle.coverage.configuration.value.AndroidConfigurationProvider
import tech.antibytes.gradle.coverage.configuration.value.JvmConfigurationProvider

internal object DefaultConfigurationProvider : CoverageContract.DefaultConfigurationProvider {
    private fun fetchConfiguration(
        project: Project,
        context: ConfigurationContract.PlatformContext
    ): CoverageApiContract.CoverageConfiguration {
        return if (context.prefix == "jvm") {
            JvmConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                context
            )
        } else {
            AndroidConfigurationProvider.createDefaultCoverageConfiguration(
                project,
                context
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
