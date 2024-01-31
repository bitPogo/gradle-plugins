/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.coverage.api

import tech.antibytes.gradle.coverage.CoverageApiContract

data class JacocoReporterSettings(
    override var useHtml: Boolean = true,
    override var useCsv: Boolean = true,
    override var useXml: Boolean = true,
) : CoverageApiContract.JacocoReporterSettings
