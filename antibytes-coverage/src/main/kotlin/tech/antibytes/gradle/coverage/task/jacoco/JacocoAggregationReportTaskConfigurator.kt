/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoReport
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.task.TaskContract

internal object JacocoAggregationReportTaskConfigurator : TaskContract.ReportTaskConfigurator, JacocoAggregationBase() {
    private fun addReportTask(
        project: Project,
        contextId: String,
        aggregator: AggregationData,
        configuration: CoverageApiContract.JacocoAggregationConfiguration
    ): JacocoReport {
        return project.tasks.create(
            "${contextId}CoverageAggregation",
            JacocoReport::class.java
        ) {
            this.group = "Verification"
            this.description = "Aggregates coverage reports for ${contextId.capitalize()}."

            if (aggregator.dependencies.isNotEmpty()) {
                this.setDependsOn(aggregator.dependencies)
                configureJacocoAggregationBase(this, aggregator)

                configureOutput(
                    project,
                    contextId,
                    configuration.reportSettings,
                    this
                )
            }
        }
    }

    override fun configure(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.CoverageConfiguration
    ): Task {
        return addReportTask(
            project,
            contextId,
            aggregate(
                project,
                contextId,
                (configuration as CoverageApiContract.JacocoAggregationConfiguration)
            ),
            configuration
        )
    }
}
