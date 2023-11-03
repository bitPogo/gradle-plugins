/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import java.math.BigDecimal
import org.gradle.testing.jacoco.tasks.rules.JacocoLimit
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRule
import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRulesContainer
import tech.antibytes.gradle.coverage.CoverageApiContract

internal class JacocoVerificationRuleMapper : JacocoContract.JacocoVerificationRuleMapper {
    private fun hasLimit(rule: CoverageApiContract.JacocoVerificationRule): Boolean {
        return rule.minimum is BigDecimal || rule.maximum is BigDecimal
    }

    private fun configureLimit(
        limit: JacocoLimit,
        rule: CoverageApiContract.JacocoVerificationRule,
    ) {
        limit.minimum = rule.minimum
        limit.maximum = rule.maximum
        limit.counter = rule.counter.name
        limit.value = rule.measurement.value
    }

    private fun mapRule(
        jacocoViolationRule: JacocoViolationRule,
        rule: CoverageApiContract.JacocoVerificationRule,
    ): JacocoViolationRule {
        jacocoViolationRule.element = rule.scope.name
        jacocoViolationRule.isEnabled = rule.enable
        jacocoViolationRule.includes = rule.includes.toList()
        jacocoViolationRule.excludes = rule.excludes.toList()

        if (hasLimit(rule)) {
            jacocoViolationRule.limit { configureLimit(this, rule) }
        }

        return jacocoViolationRule
    }

    override fun map(
        generator: JacocoViolationRulesContainer,
        rules: Set<CoverageApiContract.JacocoVerificationRule>,
    ) {
        rules.map { rule -> mapRule(generator.rule {}, rule) }
    }
}
