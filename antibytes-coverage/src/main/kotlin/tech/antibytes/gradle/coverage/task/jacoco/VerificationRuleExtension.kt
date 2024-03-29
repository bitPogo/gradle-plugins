/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

@file:Suppress("ktlint:standard:filename")

package tech.antibytes.gradle.coverage.task.jacoco

import java.math.BigDecimal
import tech.antibytes.gradle.coverage.CoverageApiContract

internal fun CoverageApiContract.JacocoVerificationRule.isValidRule(): Boolean {
    return maximum is BigDecimal || minimum is BigDecimal
}
