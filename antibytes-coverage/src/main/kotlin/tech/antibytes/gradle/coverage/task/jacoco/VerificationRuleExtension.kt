/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.task.jacoco

import tech.antibytes.gradle.coverage.CoverageApiContract
import java.math.BigDecimal

internal fun CoverageApiContract.JacocoVerificationRule.isValidRule(): Boolean {
    return maximum is BigDecimal || minimum is BigDecimal
}
