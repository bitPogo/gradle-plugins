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

class QualityGateConfigurationSpec {
    @Test
    fun `QualityGateConfigurationConfiguration it fulfils QualityGateConfiguration`() {
        val config: Any = QualityGateConfiguration(mockk(relaxed = true), "")

        assertTrue(config is QualityApiContract.QualityGateConfiguration)
    }

    @Test
    fun `Both constructors have the same default parameter`() {
        val config1 = QualityGateConfiguration(mockk(relaxed = true), "test")
        val config2 = QualityGateConfiguration(
            projectKey = "test",
            detekt = "test2",
        )

        assertEquals(
            config1.exclude,
            config2.exclude,
        )
        assertEquals(
            config1.organization,
            config2.organization,
        )
        assertEquals(
            config1.host,
            config2.host,
        )
        assertEquals(
            config1.jacoco,
            config2.jacoco,
        )
        assertEquals(
            config1.junit,
            config2.junit,
        )
    }
}
