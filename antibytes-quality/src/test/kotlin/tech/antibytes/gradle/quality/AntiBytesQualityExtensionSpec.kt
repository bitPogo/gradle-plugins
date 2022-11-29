/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality

import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.quality.api.LinterConfiguration
import tech.antibytes.gradle.test.createExtension

class AntiBytesQualityExtensionSpec {
    @Test
    fun `It fulfils Extension`() {
        val extension: Any = createExtension<AntiBytesQualityExtension>()

        assertTrue(extension is QualityContract.Extension)
    }

    @Test
    fun `It has a Linter on Default`() {
        val extension = createExtension<AntiBytesQualityExtension>()

        assertEquals(
            actual = extension.linter.orNull,
            expected = LinterConfiguration(),
        )
    }

    @Test
    fun `It has no explicit Api on Default`() {
        val extension = createExtension<AntiBytesQualityExtension>()

        assertEquals(
            actual = extension.explicitApiFor.orNull,
            expected = emptySet(),
        )
    }

    @Test
    fun `It has no CodeAnalysis on Default`() {
        val extension = createExtension<AntiBytesQualityExtension>()

        assertNull(extension.codeAnalysis.orNull)
    }

    @Test
    fun `It has no StableApi on Default`() {
        val extension = createExtension<AntiBytesQualityExtension>()

        assertNull(extension.stableApi.orNull)
    }

    @Test
    fun `It has no QualityGate on Default`() {
        val extension = createExtension<AntiBytesQualityExtension>()

        assertNull(extension.qualityGate.orNull)
    }
}
