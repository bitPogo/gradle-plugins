/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage.api

import tech.antibytes.gradle.coverage.CoverageApiContract

data class JacocoReporterSettings(
    override val useHtml: Boolean = true,
    override val useCsv: Boolean = true,
    override val useXml: Boolean = true
) : CoverageApiContract.JacocoReporterSettings
