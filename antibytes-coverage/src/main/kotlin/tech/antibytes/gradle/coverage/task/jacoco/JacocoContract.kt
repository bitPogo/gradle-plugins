/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import org.gradle.testing.jacoco.tasks.rules.JacocoViolationRulesContainer
import tech.antibytes.gradle.coverage.CoverageApiContract

internal interface JacocoContract {
    interface JacocoVerificationRuleMapper {
        fun map(
            generator: JacocoViolationRulesContainer,
            rules: Set<CoverageApiContract.JacocoVerificationRule>
        )
    }
}
