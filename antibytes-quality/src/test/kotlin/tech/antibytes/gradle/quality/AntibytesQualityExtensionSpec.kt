/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.quality

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.api.LinterConfiguration
import tech.antibytes.gradle.test.createExtension

class AntibytesQualityExtensionSpec {
    @Test
    fun `It fulfils Extension`() {
        val extension: Any = createExtension<AntibytesQualityExtension>()

        assertTrue(extension is QualityContract.Extension)
    }

    @Test
    fun `It has a Linter on Default`() {
        val extension = createExtension<AntibytesQualityExtension>()

        assertEquals(
            actual = extension.linter.orNull,
            expected = LinterConfiguration(),
        )
    }

    @Test
    fun `It has no CodeAnalysis on Default`() {
        val extension = createExtension<AntibytesQualityExtension>()

        assertNull(extension.codeAnalysis.orNull)
    }

    @Test
    fun `It has no StableApi on Default`() {
        val extension = createExtension<AntibytesQualityExtension>()

        assertNull(extension.stableApi.orNull)
    }

    @Test
    fun `It has no QualityGate on Default`() {
        val extension = createExtension<AntibytesQualityExtension>()

        assertNull(extension.qualityGate.orNull)
    }
}
