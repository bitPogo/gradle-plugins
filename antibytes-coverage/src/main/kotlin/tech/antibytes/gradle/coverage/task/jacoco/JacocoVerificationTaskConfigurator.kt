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

internal class JacocoVerificationTaskConfigurator(
    private val mapper: JacocoContract.JacocoVerificationRuleExecutor = JacocoVerificationRuleExecutor(),
) : TaskContract.VerificationTaskConfigurator, JacocoTaskBase() {
    private fun addVerificationTask(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.JacocoCoverageConfiguration,
    ): Task {
        return project.tasks.create(
            "${contextId}CoverageVerification",
            JacocoCoverageVerification::class.java,
        ) {
            group = "Verification"
            description = "Verifies the coverage reports against a given set of rules for ${contextId.capitalize()}."
            setDependsOn(
                setOf(project.tasks.getByName("${contextId}Coverage")),
            )

            configureJacocoCoverageBase(
                project,
                this,
                configuration,
                determineExecutionsFiles(configuration),
            )

            violationRules {
                isFailOnViolation = true
                mapper.apply(this, configuration.verificationRules)
            }
        }
    }

    private fun CoverageApiContract.JacocoCoverageConfiguration.hasRules(): Boolean {
        return this.verificationRules.any { rule -> rule.isValidRule() }
    }

    override fun configure(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.CoverageConfiguration,
    ): Task? {
        configuration as CoverageApiContract.JacocoCoverageConfiguration
        return if (configuration.hasRules()) {
            addVerificationTask(project, contextId, configuration)
        } else {
            null
        }
    }
}
