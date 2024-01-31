/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.task.TaskContract
import tech.antibytes.gradle.util.capitalize

internal class JacocoAggregationVerificationTaskConfigurator(
    private val mapper: JacocoContract.JacocoVerificationRuleExecutor = JacocoVerificationRuleExecutor(),
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
                mapper.apply(this, configuration.verificationRules)
            }
        }
    }

    private fun CoverageApiContract.JacocoAggregationConfiguration.hasRules(): Boolean {
        return this.verificationRules.any { rule -> rule.isValidRule() }
    }

    override fun configure(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.CoverageConfiguration,
    ): Task? {
        configuration as CoverageApiContract.JacocoAggregationConfiguration

        val aggregator = aggregate(
            project,
            contextId,
            configuration,
        )

        return if (configuration.hasRules() && aggregator.dependencies.isNotEmpty()) {
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
