/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportBase
import tech.antibytes.gradle.coverage.AntiBytesCoverageExtension
import tech.antibytes.gradle.coverage.CoverageApiContract.AndroidJacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.AndroidJacocoCoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCoverageConfiguration
import java.io.File

internal abstract class JacocoAggregationBase : JacocoTaskBase() {
    protected data class AggregationData(
        val dependencies: MutableSet<JacocoReport> = mutableSetOf(),
        val executionFiles: MutableSet<ConfigurableFileTree> = mutableSetOf(),
        val classes: MutableSet<ConfigurableFileTree> = mutableSetOf(),
        val sources: MutableSet<File> = mutableSetOf(),
        val additionalSources: MutableSet<ConfigurableFileTree> = mutableSetOf(),
        val additionalClasses: MutableSet<ConfigurableFileTree> = mutableSetOf(),
    )

    private fun resolveSubproject(
        subproject: Project,
        contextId: String,
        configuration: JacocoCoverageConfiguration,
        aggregator: AggregationData
    ) {
        aggregator.classes.add(
            subproject.fileTree(subproject.projectDir) {
                setIncludes(configuration.classPattern)
                setExcludes(configuration.classFilter)
            }
        )
        aggregator.executionFiles.add(
            subproject.fileTree(subproject.buildDir) {
                setIncludes(
                    determineExecutionsFiles(configuration)
                )
            }
        )

        aggregator.sources.addAll(configuration.sources)
        aggregator.additionalSources.addAll(configuration.additionalSources)
        aggregator.additionalClasses.addAll(configuration.additionalClasses)
        aggregator.dependencies.add(
            subproject.tasks.getByName("${contextId}Coverage") as JacocoReport
        )
    }

    private fun isMatchingJvmConfiguration(
        aggregationConfiguration: JacocoAggregationConfiguration,
        subprojectConfiguration: CoverageConfiguration?
    ): Boolean {
        return aggregationConfiguration !is AndroidJacocoAggregationConfiguration &&
            subprojectConfiguration !is AndroidJacocoCoverageConfiguration
    }

    private fun isMatchingAndroidConfiguration(
        aggregationConfiguration: JacocoAggregationConfiguration,
        subprojectConfiguration: CoverageConfiguration?
    ): Boolean {
        return aggregationConfiguration is AndroidJacocoAggregationConfiguration &&
            subprojectConfiguration is AndroidJacocoCoverageConfiguration &&
            subprojectConfiguration.flavour == aggregationConfiguration.flavour &&
            subprojectConfiguration.variant == aggregationConfiguration.variant
    }

    private fun filterAndResolveSubprojectConfiguration(
        subproject: Project,
        contextId: String,
        aggregationConfiguration: JacocoAggregationConfiguration,
        extension: AntiBytesCoverageExtension,
        aggregator: AggregationData
    ) {
        return when {
            !extension.configurations.containsKey(contextId) -> Unit
            isMatchingAndroidConfiguration(
                aggregationConfiguration,
                extension.configurations[contextId]
            ) -> resolveSubproject(
                subproject,
                contextId,
                extension.configurations[contextId] as JacocoCoverageConfiguration,
                aggregator
            )
            isMatchingJvmConfiguration(
                aggregationConfiguration,
                extension.configurations[contextId]
            ) -> resolveSubproject(
                subproject,
                contextId,
                extension.configurations[contextId] as JacocoCoverageConfiguration,
                aggregator
            )
            else -> Unit
        }
    }

    protected fun aggregate(
        project: Project,
        contextId: String,
        configuration: JacocoAggregationConfiguration
    ): AggregationData {
        val aggregator = AggregationData()
        project.subprojects.forEach { subproject ->
            val extension = subproject.extensions.findByType(AntiBytesCoverageExtension::class.java)

            if (extension is AntiBytesCoverageExtension && !configuration.exclude.contains(subproject.name)) {
                filterAndResolveSubprojectConfiguration(
                    subproject,
                    contextId,
                    configuration,
                    extension,
                    aggregator
                )
            }
        }

        return aggregator
    }

    protected fun configureJacocoAggregationBase(
        task: JacocoReportBase,
        aggregator: AggregationData
    ) {
        task.sourceDirectories.setFrom(aggregator.sources)
        task.classDirectories.setFrom(aggregator.classes)

        task.additionalSourceDirs.setFrom(aggregator.additionalSources)
        task.additionalClassDirs.setFrom(aggregator.additionalClasses)
        task.executionData.setFrom(aggregator.executionFiles)
    }
}
