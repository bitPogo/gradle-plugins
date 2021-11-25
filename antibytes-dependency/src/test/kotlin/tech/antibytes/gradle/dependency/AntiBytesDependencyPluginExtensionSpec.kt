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

class AntiBytesDependencyPluginExtensionSpec {
    @Test
    fun `It fulfils DependencyPluginExtension`() {
        val extension: Any = createExtension<AntiBytesDependencyPluginExtension>()

        assertTrue(extension is DependencyContract.DependencyPluginExtension)
    }

    @Test
    fun `It has default Keywords`() {
        val extension: AntiBytesDependencyPluginExtension = createExtension()

        assertEquals(
            actual = extension.keywords.get(),
            expected = setOf("RELEASE", "FINAL", "GA")
        )
    }

    @Test
    fun `It has default StableRegex`() {
        val extension: AntiBytesDependencyPluginExtension = createExtension()

        assertEquals(
            actual = extension.versionRegex.get().pattern,
            expected = "^[0-9,.v-]+(-r)?\$"
        )
    }
}
