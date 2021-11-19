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

internal object JacocoVerificationTaskConfigurator : TaskContract.VerificationTaskConfigurator, JacocoTaskBase() {
    private fun addVerificationTask(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.JacocoCoverageConfiguration
    ): Task {
        return project.tasks.create(
            "${contextId}CoverageVerification",
            JacocoCoverageVerification::class.java
        ) {
            group = "Verification"
            description = "Verifies the coverage reports against a given set of rules for ${contextId.capitalize()}."
            setDependsOn(
                setOf(project.tasks.getByName("${contextId}Coverage"))
            )

            configureJacocoCoverageBase(
                project,
                this,
                configuration,
                determineExecutionsFiles(configuration),
            )

            violationRules {
                isFailOnViolation = true
                JacocoVerificationRuleMapper.map(this, configuration.verificationRules)
            }
        }
    }

    override fun configure(
        project: Project,
        contextId: String,
        configuration: CoverageApiContract.CoverageConfiguration
    ): Task? {
        val rules = (configuration as CoverageApiContract.JacocoCoverageConfiguration).verificationRules
            .filter { rule -> rule.isValidRule() }

        return if (rules.isNotEmpty()) {
            addVerificationTask(project, contextId, configuration)
        } else {
            null
        }
    }
}
