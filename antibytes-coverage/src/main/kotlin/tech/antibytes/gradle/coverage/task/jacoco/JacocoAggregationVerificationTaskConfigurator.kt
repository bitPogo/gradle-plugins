/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.task.TaskContract
import tech.antibytes.gradle.util.capitalize

internal class JacocoAggregationVerificationTaskConfigurator(
    private val mapper: JacocoContract.JacocoVerificationRuleMapper = JacocoVerificationRuleMapper(),
) : TaskContract.VerificationTaskConfigurator, JacocoAggregationBase() {
    private fun addVerificationTask(
        project: Project,
        contextId: String,
        aggregation: AggregationData,
        configuration: CoverageApiContract.JacocoAggregationConfiguration,
    ): Task {
        return project.tasks.create(
            "${contextId}AggregationVerification",
            JacocoCoverageVerification::class.java,
        ) {
            group = "Verification"
            description = "Verifies the aggregated coverage reports against a given set of rules for ${contextId.capitalize()}."
            println(aggregation.dependencies)
            setDependsOn(aggregation.dependencies)

            configureJacocoAggregationBase(
                this,
                aggregation,
            )

            violationRules {
                isFailOnViolation = true
                mapper.map(this, configuration.verificationRules)
            }
        }
    }

    override fun configure(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.CoverageConfiguration,
    ): Task? {
        val rules = (configuration as CoverageApiContract.JacocoAggregationConfiguration).verificationRules
            .filter { rule -> rule.isValidRule() }

        val aggregator = aggregate(
            project,
            contextId,
            configuration,
        )

        return if (rules.isNotEmpty() && aggregator.dependencies.isNotEmpty()) {
            addVerificationTask(
                project,
                contextId,
                aggregator,
                configuration,
            )
        } else {
            null
        }
    }
}
