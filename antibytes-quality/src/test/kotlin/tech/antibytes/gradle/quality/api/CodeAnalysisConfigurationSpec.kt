/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality.api

import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.QualityApiContract

class CodeAnalysisConfigurationSpec {
    @Test
    fun `CodeAnalysisConfiguration it fulfils CodeAnalysisConfiguration`() {
        val config: Any = CodeAnalysisConfiguration(mockk(relaxed = true))

        assertTrue(config is QualityApiContract.CodeAnalysisConfiguration)
    }

    @Test
    fun `Both constructors have the same default parameter`() {
        val config1 = CodeAnalysisConfiguration(mockk(relaxed = true))
        val config2 = CodeAnalysisConfiguration(
            configurationFiles = mockk(),
            baselineFile = mockk(),
            sourceFiles = mockk(),
        )

        assertEquals(
            config1.autoCorrection,
            config2.autoCorrection,
        )
        assertEquals(
            config1.reports,
            config2.reports,
        )
        assertEquals(
            config1.exclude,
            config2.exclude,
        )
        assertEquals(
            config1.excludeBaseline,
            config2.excludeBaseline,
        )
    }
}
