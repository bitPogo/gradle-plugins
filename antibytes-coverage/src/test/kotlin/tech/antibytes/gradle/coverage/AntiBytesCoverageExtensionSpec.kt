/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.junit.Test
import tech.antibytes.gradle.publishing.createExtension
import kotlin.test.assertTrue

class AntiBytesCoverageExtensionSpec {
    @Test
    fun `It fulfils Extension`() {
        val extension: Any = createExtension<AntiBytesCoverageExtension>()

        assertTrue(extension is CoverageContract.Extension)
    }
}
