/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.coverage

import org.gradle.api.Plugin
import org.junit.Test
import kotlin.test.assertTrue

class AntiBytesCoverageSpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesCoverage()

        assertTrue(plugin is Plugin<*>)
    }
}
