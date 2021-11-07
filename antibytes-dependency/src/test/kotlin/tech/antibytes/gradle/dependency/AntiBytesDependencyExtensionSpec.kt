/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency

import org.junit.Test
import tech.antibytes.gradle.publishing.createExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AntiBytesDependencyExtensionSpec {
    @Test
    fun `It fulfils DependencyExtension`() {
        val extension: Any = createExtension<AntiBytesDependencyExtension>()

        assertTrue(extension is DependencyContract.Extension)
    }

    @Test
    fun `It has default Keywords`() {
        val extension: AntiBytesDependencyExtension = createExtension()

        assertEquals(
            actual = extension.keywords.get(),
            expected = setOf("RELEASE", "FINAL", "GA")
        )
    }

    @Test
    fun `It has default StableRegex`() {
        val extension: AntiBytesDependencyExtension = createExtension()

        assertEquals(
            actual = extension.versionRegex.get().pattern,
            expected = "^[0-9,.v-]+(-r)?\$"
        )
    }
}
