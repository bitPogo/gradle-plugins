/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.dependency.helper

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import tech.antibytes.gradle.test.createExtension

class AntibytesDependencyHelperPluginExtensionSpec {
    @Test
    fun `It fulfils DependencyPluginExtension`() {
        val extension: Any = createExtension<AntibytesDependencyPluginExtension>()

        assertTrue(extension is DependencyContract.DependencyPluginExtension)
    }

    @Test
    fun `It has default Keywords`() {
        val extension: AntibytesDependencyPluginExtension = createExtension()

        assertEquals(
            actual = extension.keywords.get(),
            expected = setOf("RELEASE", "FINAL", "GA"),
        )
    }

    @Test
    fun `It has default StableRegex`() {
        val extension: AntibytesDependencyPluginExtension = createExtension()

        assertEquals(
            actual = extension.versionRegex.get().pattern,
            expected = "^[0-9,.v-]+(-r)?\$",
        )
    }
}
