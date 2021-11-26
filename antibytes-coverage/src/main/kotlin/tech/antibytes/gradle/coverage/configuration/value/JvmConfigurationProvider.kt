/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.configuration.value

import org.gradle.api.Project
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.api.JacocoReporterSettings
import tech.antibytes.gradle.coverage.api.JvmJacocoConfiguration
import tech.antibytes.gradle.coverage.configuration.ConfigurationContract
import tech.antibytes.gradle.coverage.configuration.makePath
import tech.antibytes.gradle.coverage.source.SourceHelper
import tech.antibytes.gradle.util.GradleUtilApiContract.PlatformContext
import tech.antibytes.gradle.util.isKmp

internal object JvmConfigurationProvider : ConfigurationContract.DefaultPlatformConfigurationProvider {
    private fun resolveTestDependency(
        context: PlatformContext
    ): Set<String> {
        return if (context.isKmp()) {
            setOf("${context.prefix}Test")
        } else {
            setOf("test")
        }
    }

    private fun resolveClassPattern(
        context: PlatformContext
    ): Set<String> {
        return if (context.isKmp()) {
            setOf(
                makePath("build", "classes", "java", "jvm", "main", "**", "*.class"),
                makePath("build", "classes", "kotlin", "jvm", "main", "**", "*.class")
            )
        } else {
            setOf(
                makePath("build", "classes", "java", "main", "**", "*.class"),
                makePath("build", "classes", "kotlin", "main", "**", "*.class")
            )
        }
    }

    private fun resolveClassFilter(
        context: PlatformContext
    ): Set<String> {
        return if (context.isKmp()) {
            setOf(
                makePath("build", "classes", "java", "jvm", "test", "**", "*.*"),
                makePath("build", "classes", "kotlin", "jvm", "test", "**", "*.*")
            )
        } else {
            setOf(
                makePath("build", "classes", "java", "test", "**", "*.*"),
                makePath("build", "classes", "kotlin", "test", "**", "*.*")
            )
        }
    }

    override fun createDefaultCoverageConfiguration(
        project: Project,
        context: PlatformContext
    ): CoverageApiContract.JacocoCoverageConfiguration {
        return JvmJacocoConfiguration(
            reportSettings = JacocoReporterSettings(),
            testDependencies = resolveTestDependency(context),
            classPattern = resolveClassPattern(context),
            classFilter = resolveClassFilter(context),
            sources = SourceHelper.resolveSources(project, context),
            additionalClasses = emptySet(),
            additionalSources = emptySet(),
            verificationRules = emptySet()
        )
    }
}
