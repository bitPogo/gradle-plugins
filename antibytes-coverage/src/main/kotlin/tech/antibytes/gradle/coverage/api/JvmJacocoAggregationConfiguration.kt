/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.api

import tech.antibytes.gradle.coverage.CoverageApiContract

data class JvmJacocoAggregationConfiguration(
    override val reportSettings: CoverageApiContract.JacocoReporterSettings = JacocoReporterSettings(),
    override var exclude: Set<String> = emptySet(),
    override var verificationRules: Set<CoverageApiContract.JacocoVerificationRule> = emptySet(),
) : CoverageApiContract.JacocoAggregationConfiguration
