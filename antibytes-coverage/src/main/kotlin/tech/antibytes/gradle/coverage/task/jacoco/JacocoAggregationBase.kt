/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import java.io.File
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileTree
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportBase
import tech.antibytes.gradle.coverage.AntiBytesCoveragePluginExtension
import tech.antibytes.gradle.coverage.CoverageApiContract.AndroidJacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.AndroidJacocoCoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.CoverageConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoAggregationConfiguration
import tech.antibytes.gradle.coverage.CoverageApiContract.JacocoCoverageConfiguration

internal abstract class JacocoAggregationBase : JacocoTaskBase() {
    protected data class AggregationData(
        val dependencies: MutableSet<JacocoReport> = mutableSetOf(),
        val executionFiles: MutableSet<ConfigurableFileTree> = mutableSetOf(),
        val classes: MutableSet<ConfigurableFileTree> = mutableSetOf(),
        val sources: MutableSet<File> = mutableSetOf(),
        val additionalSources: MutableSet<File> = mutableSetOf(),
        var additionalClasses: MutableList<ConfigurableFileTree> = mutableListOf(),
    )

    private fun resolveSubproject(
        subproject: Project,
        contextId: String,
        configuration: JacocoCoverageConfiguration,
        aggregator: AggregationData,
    ) {
        aggregator.classes.add(
            subproject.fileTree(subproject.projectDir) {
                setIncludes(configuration.classPattern)
                setExcludes(configuration.classFilter)
            },
        )
        aggregator.executionFiles.add(
            subproject.fileTree(subproject.buildDir) {
                setIncludes(
                    determineExecutionsFiles(configuration),
                )
            },
        )

        aggregator.sources.addAll(configuration.sources)
        aggregator.additionalSources.addAll(configuration.additionalSources)

        if (configuration.additionalClasses is ConfigurableFileTree) {
            aggregator.additionalClasses.add(configuration.additionalClasses!!)
        }

        aggregator.dependencies.add(
            subproject.tasks.getByName("${contextId}Coverage") as JacocoReport,
        )
    }

    private fun isMatchingJvmConfiguration(
        aggregationConfiguration: JacocoAggregationConfiguration,
        subprojectConfiguration: CoverageConfiguration?,
    ): Boolean {
        return aggregationConfiguration !is AndroidJacocoAggregationConfiguration &&
            subprojectConfiguration !is AndroidJacocoCoverageConfiguration
    }

    private fun isMatchingAndroidConfiguration(
        aggregationConfiguration: JacocoAggregationConfiguration,
        subprojectConfiguration: CoverageConfiguration?,
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
        extension: AntiBytesCoveragePluginExtension,
        aggregator: AggregationData,
    ) {
        return when {
            !extension.configurations.containsKey(contextId) -> Unit
            isMatchingAndroidConfiguration(
                aggregationConfiguration,
                extension.configurations[contextId],
            ) -> resolveSubproject(
                subproject,
                contextId,
                extension.configurations[contextId] as JacocoCoverageConfiguration,
                aggregator,
            )
            isMatchingJvmConfiguration(
                aggregationConfiguration,
                extension.configurations[contextId],
            ) -> resolveSubproject(
                subproject,
                contextId,
                extension.configurations[contextId] as JacocoCoverageConfiguration,
                aggregator,
            )
            else -> Unit
        }
    }

    protected fun aggregate(
        project: Project,
        contextId: String,
        configuration: JacocoAggregationConfiguration,
    ): AggregationData {
        val aggregator = AggregationData()
        project.subprojects.forEach { subproject ->
            val extension = subproject.extensions.findByType(AntiBytesCoveragePluginExtension::class.java)

            if (extension is AntiBytesCoveragePluginExtension && !configuration.exclude.contains(subproject.name)) {
                filterAndResolveSubprojectConfiguration(
                    subproject,
                    contextId,
                    configuration,
                    extension,
                    aggregator,
                )
            }
        }

        return aggregator
    }

    private fun resolveAdditionalClasses(
        aggregatedAdditionalClasses: MutableList<ConfigurableFileTree>,
    ): FileTree {
        var additionalClassFileTree: FileTree = aggregatedAdditionalClasses.removeAt(0)
        aggregatedAdditionalClasses.forEach { subTree ->
            additionalClassFileTree = additionalClassFileTree.plus(subTree)
        }

        return additionalClassFileTree
    }

    protected fun configureJacocoAggregationBase(
        task: JacocoReportBase,
        aggregator: AggregationData,
    ) {
        task.sourceDirectories.setFrom(aggregator.sources)
        task.classDirectories.setFrom(aggregator.classes)

        task.additionalSourceDirs.setFrom(aggregator.additionalSources)

        val aggregatedAdditionalClasses = if (aggregator.additionalClasses.isEmpty()) {
            emptySet<File>()
        } else {
            resolveAdditionalClasses(aggregator.additionalClasses)
        }

        task.additionalClassDirs.setFrom(aggregatedAdditionalClasses)
        task.executionData.setFrom(aggregator.executionFiles)
    }
}
