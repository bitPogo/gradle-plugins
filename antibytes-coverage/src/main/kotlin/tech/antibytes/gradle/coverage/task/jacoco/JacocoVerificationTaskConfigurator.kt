/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import tech.antibytes.gradle.coverage.CoverageApiContract
import tech.antibytes.gradle.coverage.task.TaskContract
import java.math.BigDecimal

internal object JacocoVerificationTaskConfigurator : TaskContract.VerificationTaskConfigurator, JacocoTaskBase() {
    private fun isValidRule(rule: CoverageApiContract.JacocoVerificationRule): Boolean {
        return rule.maximum is BigDecimal || rule.minimum is BigDecimal
    }

    private fun addVerificationTask(
        project: Project,
        contextName: String,
        configuration: CoverageApiContract.JacocoCoverageConfiguration
    ): Task {
        return project.tasks.create(
            "${contextName}CoverageVerification",
            JacocoCoverageVerification::class.java
        ) {
            group = "Verification"
            description = "Verifies the coverage reports against a given set of rules for ${contextName.capitalize()}."
            setDependsOn(
                setOf(project.tasks.getByName("${contextName}Coverage"))
            )

            configureJacocoCoverageBase(
                configuration,
                determineExecutionsFiles(configuration),
                project,
                this
            )

            violationRules {
                isFailOnViolation = true
                JacocoVerificationRuleMapper.map(this, configuration.verificationRules)
            }
        }
    }

    override fun configure(
        project: Project,
        contextName: String,
        configuration: CoverageApiContract.CoverageConfiguration
    ): Task? {
        val rules = (configuration as CoverageApiContract.JacocoCoverageConfiguration).verificationRules
            .filter { rule -> isValidRule(rule) }

        return if (rules.isNotEmpty()) {
            addVerificationTask(project, contextName, configuration)
        } else {
            null
        }
    }
}
