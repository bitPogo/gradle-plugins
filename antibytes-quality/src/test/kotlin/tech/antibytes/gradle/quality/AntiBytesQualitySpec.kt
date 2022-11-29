/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache License, Version 2.0
 */

package tech.antibytes.gradle.quality

import org.gradle.api.Plugin
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class AntiBytesQualitySpec {
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = AntiBytesQuality()

        assertTrue(plugin is Plugin<*>)
    }
}
