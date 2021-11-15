/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.api

import tech.antibytes.gradle.coverage.CoverageApiContract
import java.math.BigDecimal

data class JacocoVerificationRule(
    override val scope: CoverageApiContract.JacocoScope = CoverageApiContract.JacocoScope.BUNDLE,
    override val enable: Boolean = true,
    override val counter: CoverageApiContract.JacocoCounter = CoverageApiContract.JacocoCounter.INSTRUCTION,
    override val measurement: CoverageApiContract.JacocoMeasurement = CoverageApiContract.JacocoMeasurement.COVERED_RATIO,
    override val minimum: BigDecimal? = null,
    override val maximum: BigDecimal? = null,
    override val includes: Set<String> = setOf("*"),
    override val excludes: Set<String> = emptySet()
) : CoverageApiContract.JacocoVerificationRule
